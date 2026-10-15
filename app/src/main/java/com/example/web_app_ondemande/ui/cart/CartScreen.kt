package com.example.web_app_ondemande.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme
import com.example.web_app_ondemande.util.formatRupiah
import kotlinx.coroutines.launch

/**
 * Halaman Utama Ringkasan Keranjang / Checkout Onde Mande.
 *
 * Fitur Compose:
 * - Scaffold dengan bottomBar & SnackbarHost untuk konfirmasi pesanan.
 * - SingleChoiceSegmentedButtonRow (Material 3) untuk opsi pengambilan ("Ambil di tempat" & "Antar").
 * - OutlinedTextField dengan rememberSaveable & imePadding agar input catatan dapur tidak tertutup keyboard.
 * - LazyColumn dengan items(key = { it.id }) dan Modifier.animateItem() untuk animasi penghapusan item yang mulus.
 * - State Hoisting terpusat untuk sinkronisasi jumlah porsi lintas halaman.
 *
 * Bagian Tampilan:
 * - Menampilkan item pesanan, opsi pengiriman, catatan dapur, ringkasan pembayaran, dan konfirmasi checkout.
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
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedDeliveryOption by rememberSaveable { mutableIntStateOf(0) } // 0: Ambil di tempat, 1: Antar
    var kitchenNotes by rememberSaveable { mutableStateOf("") }

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
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = { showConfirmationDialog = true },
                        modifier = Modifier
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
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
            } else {
                // Tampilan Keranjang Berisi Item
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Judul Halaman Serif
                    item(key = "header_title") {
                        Column(
                            modifier = Modifier
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
                                text = "Siap Disantap",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

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
                        Column(
                            modifier = Modifier.fillMaxWidth(),
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
                                deliveryOptions.forEachIndexed { index, label ->
                                    SegmentedButton(
                                        selected = selectedDeliveryOption == index,
                                        onClick = { selectedDeliveryOption = index },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = index,
                                            count = deliveryOptions.size
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

                    // Input Catatan untuk Dapur
                    item(key = "kitchen_notes") {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Catatan untuk Dapur",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = kitchenNotes,
                                onValueChange = { kitchenNotes = it },
                                placeholder = {
                                    Text(
                                        text = "Contoh: sambal dipisah, rendang tanpa kuah",
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                },
                                maxLines = 2,
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
                    Text(text = "Rincian Pesanan:")
                    Text(
                        text = orderSummaryText,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Opsi: ${deliveryOptions[selectedDeliveryOption]}",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    if (kitchenNotes.isNotBlank()) {
                        Text(
                            text = "Catatan: \"$kitchenNotes\"",
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
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

@Preview(showBackground = true)
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
