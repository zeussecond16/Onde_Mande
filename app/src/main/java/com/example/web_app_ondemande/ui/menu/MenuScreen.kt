package com.example.web_app_ondemande.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Halaman Katalog Menu Utama Onde Mande.
 *
 * Fitur Compose:
 * - Scaffold untuk mengelola struktur halaman dan penempatan CartBar di bottomBar.
 * - LazyColumn + items(key = { it.id }) untuk merender daftar makanan secara efisien.
 * - BadgedBox & Badge pada header top bar untuk memperlihatkan jumlah item keranjang secara langsung.
 * - derivedStateOf untuk menghitung total item dan total harga keranjang secara reaktif.
 *
 * Bagian Tampilan:
 * - Menampilkan daftar 10 hidangan Minang beserta stepper kuantitas, ikon badge keranjang di header, dan bar keranjang melayang.
 */
@Composable
fun MenuScreen(
    cartState: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Hitung total item & total harga secara efisien menggunakan derivedStateOf
    val totalItems by remember(cartState) {
        derivedStateOf { cartState.values.sum() }
    }
    val totalPrice by remember(cartState) {
        derivedStateOf {
            cartState.entries.sumOf { (id, qty) ->
                val menuItem = MenuData.items.find { it.id == id }
                (menuItem?.price ?: 0) * qty
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OndeMandeCream,
        bottomBar = {
            CartBar(
                totalItems = totalItems,
                totalPrice = totalPrice,
                onCartClick = onNavigateToCart
            )
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
                // Tombol Kembali ‹
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onNavigateBack() },
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
                    text = "Onde Mande",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Ikon Keranjang + Badge Jumlah Item
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onNavigateToCart() },
                    contentAlignment = Alignment.Center
                ) {
                    BadgedBox(
                        badge = {
                            if (totalItems > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(
                                        text = totalItems.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "🛒",
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // Judul Halaman Menu
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Pilih Hidangan,",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Langsuang Dari Dapua Kami",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }

            // Daftar Katalog Makanan (LazyColumn)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = MenuData.items,
                    key = { it.id }
                ) { menuItem ->
                    val quantity = cartState[menuItem.id] ?: 0
                    MenuItemCard(
                        item = menuItem,
                        quantity = quantity,
                        onIncrease = { onIncrease(menuItem.id) },
                        onDecrease = { onDecrease(menuItem.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    OndeMandeTheme {
        MenuScreen(
            cartState = mapOf(1 to 1, 3 to 2),
            onIncrease = {},
            onDecrease = {},
            onNavigateToCart = {},
            onNavigateBack = {}
        )
    }
}
