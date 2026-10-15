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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.web_app_ondemande.ui.theme.OndeMandeCream
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeMuted
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Halaman Ringkasan Keranjang / Checkout Onde Mande.
 *
 * Fitur Compose:
 * - Scaffold untuk mengelola struktur halaman dan tombol konfirmasi di bottomBar.
 * - LazyColumn untuk daftar item yang dipesan.
 * - AlertDialog konfirmasi pesanan (pemicu simulasi tanpa backend).
 * - State mutabel showDialog untuk mengontrol kemunculan dialog konfirmasi.
 *
 * Bagian Tampilan:
 * - Menampilkan daftar item dalam keranjang, rincian subtotal & total harga, serta konfirmasi pesanan.
 */
@Composable
fun CartScreen(
    cartState: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onClearCart: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Ambil daftar item yang dipilih (qty > 0)
    val cartItems by remember(cartState) {
        derivedStateOf {
            MenuData.items.filter { (cartState[it.id] ?: 0) > 0 }
        }
    }

    val totalPrice by remember(cartState) {
        derivedStateOf {
            cartState.entries.sumOf { (id, qty) ->
                val item = MenuData.items.find { it.id == id }
                (item?.price ?: 0) * qty
            }
        }
    }

    val totalItems by remember(cartState) {
        derivedStateOf { cartState.values.sum() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OndeMandeCream,
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Subtotal ($totalItems item)",
                                style = MaterialTheme.typography.bodyLarge,
                                color = OndeMandeMuted
                            )
                            Text(
                                text = MenuData.formatRupiah(totalPrice),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HorizontalDivider(color = OndeMandeMuted.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Pembayaran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = MenuData.formatRupiah(totalPrice),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = OndeMandeInk
                            )
                        }

                        Button(
                            onClick = { showConfirmationDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OndeMandeInk)
                        ) {
                            Text(
                                text = "Konfirmasi & Pesan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                        color = OndeMandeInk,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = "Keranjang Pesanan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Box(modifier = Modifier.size(40.dp))
            }

            if (cartItems.isEmpty()) {
                // Tampilan Keranjang Kosong
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Keranjang Anda Masih Kosong",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pilih masakan Minang lezat kesukaan Anda di halaman menu.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OndeMandeMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBackToMenu,
                        colors = ButtonDefaults.buttonColors(containerColor = OndeMandeInk),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(text = "Lihat Menu Makanan", color = Color.White)
                    }
                }
            } else {
                // Daftar Item Pesanan
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = cartItems,
                        key = { it.id }
                    ) { item ->
                        val qty = cartState[item.id] ?: 0
                        CartItemRow(
                            item = item,
                            quantity = qty,
                            onIncrease = { onIncrease(item.id) },
                            onDecrease = { onDecrease(item.id) }
                        )
                    }
                }
            }
        }
    }

    // Dialog Konfirmasi Pesanan (Simulasi)
    if (showConfirmationDialog) {
        val orderSummaryText = cartItems.joinToString(", ") {
            "${cartState[it.id]}x ${it.name}"
        }

        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(
                    text = "Pesanan Berhasil Disiapkan! 🎉",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Rincian Pesanan Anda:")
                    Text(
                        text = orderSummaryText,
                        fontWeight = FontWeight.SemiBold,
                        color = OndeMandeInk
                    )
                    Text(
                        text = "Total Pembayaran: ${MenuData.formatRupiah(totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = OndeMandeInk
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Terima kasih telah memesan di Onde Mande. Hidangan hangat akan segera diantarkan ke meja/rumah Anda!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OndeMandeMuted
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        onClearCart()
                        onBackToMenu()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OndeMandeInk)
                ) {
                    Text(text = "Selesai", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text(text = "Batal", color = OndeMandeInk)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    OndeMandeTheme {
        CartScreen(
            cartState = mapOf(1 to 2, 3 to 1),
            onIncrease = {},
            onDecrease = {},
            onClearCart = {},
            onBackToMenu = {}
        )
    }
}
