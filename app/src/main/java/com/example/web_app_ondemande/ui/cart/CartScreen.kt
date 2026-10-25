package com.example.web_app_ondemande.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.ui.adaptive.ContentMaxWidth
import com.example.web_app_ondemande.ui.adaptive.SingleColumnMaxWidth
import com.example.web_app_ondemande.ui.adaptive.WindowWidthClass
import com.example.web_app_ondemande.ui.adaptive.currentWidthClass
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme
import com.example.web_app_ondemande.util.Validation
import com.example.web_app_ondemande.util.formatRupiah
import kotlinx.coroutines.launch

/**
 * Halaman Ringkasan Keranjang / Checkout Onde Mande (ADAPTIF + RESPONSIF).
 *
 * Tata letak berubah sesuai lebar layar (currentWidthClass):
 * - Compact / Medium : satu kolom (daftar item -> opsi -> form -> catatan -> ringkasan),
 *                      lebarnya dibatasi SingleColumnMaxWidth dan diletakkan di tengah.
 * - Expanded (tablet lebar): DUA PANEL berdampingan.
 *     Kiri  = daftar item pesanan (LazyColumn sendiri).
 *     Kanan = opsi pengambilan, data pemesan, catatan dapur, ringkasan (scroll sendiri).
 *
 * State (jumlah porsi, isi form) tetap di sini, jadi tidak hilang ketika layar diputar
 * dan tampilan berpindah antara satu kolom <-> dua panel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartState: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onConfirmOrder: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val widthClass = currentWidthClass()
    val isTwoPane = widthClass == WindowWidthClass.Expanded

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val rightPaneScrollState = rememberScrollState()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedDeliveryOption by rememberSaveable { mutableIntStateOf(0) } // 0: Ambil di tempat, 1: Antar
    val isDelivery = selectedDeliveryOption == 1

    // State Form Data Pemesan (rememberSaveable agar tahan rotasi layar)
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var kitchenNotes by rememberSaveable { mutableStateOf("") }
    var submitAttempted by rememberSaveable { mutableStateOf(false) }

    // Evaluasi Pesan Error (Hanya jika submitAttempted == true)
    val nameError = if (submitAttempted) Validation.validateName(name) else null
    val phoneError = if (submitAttempted) Validation.validatePhone(phone) else null
    val addressError = if (submitAttempted) Validation.validateAddress(address, isDelivery) else null
    val isFormValid = nameError == null && phoneError == null && addressError == null

    val deliveryOptions = listOf("Ambil di tempat", "Antar")

    // Filter item yang dipesan (qty > 0)
    val cartItems by remember(cartState) {
        derivedStateOf {
            MenuData.items.filter { (cartState[it.id] ?: 0) > 0 }
        }
    }

    val totalItems by remember(cartState) {
        derivedStateOf { cartState.values.sum() }
    }

    val totalPrice by remember(cartState) {
        derivedStateOf {
            cartState.entries.sumOf { (id, qty) ->
                val item = MenuData.items.find { it.id == id }
                (item?.price ?: 0) * qty
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                // Bottom Bar Tombol Pill Hitam "Pesan Sekarang • Rp xx.xxx"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (!isFormValid) {
                                submitAttempted = true
                                // Gulir ke bagian form pemesan jika ada error
                                coroutineScope.launch {
                                    if (isTwoPane) {
                                        rightPaneScrollState.animateScrollTo(0)
                                    } else {
                                        // urutan: judul(0), item pesanan, opsi pengambilan, form
                                        lazyListState.animateScrollToItem(index = cartItems.size + 2)
                                    }
                                }
                            } else {
                                focusManager.clearFocus()
                                showConfirmationDialog = true
                            }
                        },
                        modifier = Modifier
                            .widthIn(max = SingleColumnMaxWidth)
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            text = "Pesan Sekarang  •  ${formatRupiah(totalPrice)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Konten diletakkan di tengah dan dibatasi lebarnya
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = if (isTwoPane) ContentMaxWidth else SingleColumnMaxWidth)
                    .fillMaxSize()
            ) {
                // Header Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tombol Kembali Bulat Putih
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onBackToMenu() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    Text(
                        text = "Onde Mande",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Penyeimbang layout
                    Box(modifier = Modifier.size(40.dp))
                }

                if (cartItems.isEmpty()) {
                    // Tampilan Keranjang Kosong
                    EmptyCartState(onNavigateToMenu = onBackToMenu)
                } else if (isTwoPane) {
                    // ===== TABLET LEBAR: DUA PANEL =====
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Panel kiri: judul + daftar item pesanan
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item(key = "header_title") { CartTitle() }

                            items(
                                items = cartItems,
                                key = { it.id }
                            ) { item ->
                                val qty = cartState[item.id] ?: 0
                                CartItemCard(
                                    item = item,
                                    quantity = qty,
                                    onIncrease = { onIncrease(item.id) },
                                    onDecrease = { onDecrease(item.id) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }

                        // Panel kanan: opsi, form, catatan, ringkasan (scroll sendiri)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(rightPaneScrollState)
                                .padding(top = 8.dp, bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DeliveryOptionSection(
                                options = deliveryOptions,
                                selectedIndex = selectedDeliveryOption,
                                onSelect = { selectedDeliveryOption = it }
                            )
                            CustomerFormSection(
                                name = name,
                                onNameChange = { name = it },
                                nameError = nameError,
                                phone = phone,
                                onPhoneChange = { phone = it },
                                phoneError = phoneError,
                                address = address,
                                onAddressChange = { address = it },
                                addressError = addressError,
                                isDelivery = isDelivery,
                                focusManager = focusManager
                            )
                            KitchenNotesSection(
                                value = kitchenNotes,
                                onValueChange = { kitchenNotes = it }
                            )
                            OrderSummaryCard(
                                totalItems = totalItems,
                                totalPrice = totalPrice
                            )
                        }
                    }
                } else {
                    // ===== HP & TABLET KECIL: SATU KOLOM =====
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Judul Halaman Serif
                        item(key = "header_title") { CartTitle() }

                        // Daftar Item Pesanan dengan Animasi Hapus Mulus
                        items(
                            items = cartItems,
                            key = { it.id }
                        ) { item ->
                            val qty = cartState[item.id] ?: 0
                            CartItemCard(
                                item = item,
                                quantity = qty,
                                onIncrease = { onIncrease(item.id) },
                                onDecrease = { onDecrease(item.id) },
                                modifier = Modifier.animateItem()
                            )
                        }

                        // Opsi Pengambilan (Segmented Button Row Material 3)
                        item(key = "delivery_options") {
                            DeliveryOptionSection(
                                options = deliveryOptions,
                                selectedIndex = selectedDeliveryOption,
                                onSelect = { selectedDeliveryOption = it }
                            )
                        }

                        // Form Data Pemesan (Nama, Telepon, Alamat bersyarat)
                        item(key = "customer_form") {
                            CustomerFormSection(
                                name = name,
                                onNameChange = { name = it },
                                nameError = nameError,
                                phone = phone,
                                onPhoneChange = { phone = it },
                                phoneError = phoneError,
                                address = address,
                                onAddressChange = { address = it },
                                addressError = addressError,
                                isDelivery = isDelivery,
                                focusManager = focusManager
                            )
                        }

                        // Input Catatan untuk Dapur
                        item(key = "kitchen_notes") {
                            KitchenNotesSection(
                                value = kitchenNotes,
                                onValueChange = { kitchenNotes = it }
                            )
                        }

                        // Kartu Ringkasan Total Pembayaran
                        item(key = "order_summary") {
                            OrderSummaryCard(
                                totalItems = totalItems,
                                totalPrice = totalPrice
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    // Dialog Konfirmasi Pesanan
    if (showConfirmationDialog) {
        val orderSummaryText = cartItems.joinToString(", ") {
            "${cartState[it.id]}x ${it.name}"
        }

        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(
                    text = "Konfirmasi Pesanan",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Rincian Pemesan:")
                    Text(
                        text = "Nama: ${name.trim()}",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Telepon: ${phone.trim()}",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Opsi: ${deliveryOptions[selectedDeliveryOption]}",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    if (isDelivery && address.isNotBlank()) {
                        Text(
                            text = "Alamat: ${address.trim()}",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (kitchenNotes.isNotBlank()) {
                        Text(
                            text = "Catatan Dapur: \"${kitchenNotes.trim()}\"",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Daftar Item: $orderSummaryText",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Total Pembayaran: ${formatRupiah(totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        onConfirmOrder()

                        // Reset seluruh form lokal
                        name = ""
                        phone = ""
                        address = ""
                        kitchenNotes = ""
                        selectedDeliveryOption = 0
                        submitAttempted = false

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Pesanan diterima, terima kasih!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Ya, Pesan Sekarang", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text(text = "Batal", color = MaterialTheme.colorScheme.primary)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

/** Judul halaman keranjang (dua baris, rata tengah). */
@Composable
private fun CartTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Pesananmu,",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Siap Disantok",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

/** Opsi Pengambilan (Ambil di tempat / Antar) memakai Segmented Button Material 3. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeliveryOptionSection(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Opsi Pengambilan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = selectedIndex == index,
                    onClick = { onSelect(index) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        inactiveContainerColor = Color.White,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = label, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/** Input Catatan untuk Dapur. */
@Composable
private fun KitchenNotesSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Catatan untuk Dapur",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Contoh: sambal dipisah, rendang tanpa kuah",
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "HP", showBackground = true)
@Composable
fun CartScreenPreviewWithItems() {
    OndeMandeTheme {
        CartScreen(
            cartState = mapOf(1 to 2, 3 to 1),
            onIncrease = {},
            onDecrease = {},
            onConfirmOrder = {},
            onBackToMenu = {}
        )
    }
}

@Preview(name = "Tablet Portrait", showBackground = true, widthDp = 700, heightDp = 1000)
@Composable
fun CartScreenTabletPortraitPreview() {
    OndeMandeTheme {
        CartScreen(
            cartState = mapOf(1 to 2, 3 to 1),
            onIncrease = {},
            onDecrease = {},
            onConfirmOrder = {},
            onBackToMenu = {}
        )
    }
}

@Preview(name = "Tablet Landscape (2 panel)", showBackground = true, widthDp = 1024, heightDp = 700)
@Composable
fun CartScreenTabletPreview() {
    OndeMandeTheme {
        CartScreen(
            cartState = mapOf(1 to 2, 3 to 1),
            onIncrease = {},
            onDecrease = {},
            onConfirmOrder = {},
            onBackToMenu = {}
        )
    }
}