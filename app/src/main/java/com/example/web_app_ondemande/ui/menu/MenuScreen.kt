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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
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
import com.example.web_app_ondemande.ui.adaptive.ContentMaxWidth
import com.example.web_app_ondemande.ui.adaptive.WindowWidthClass
import com.example.web_app_ondemande.ui.adaptive.currentWidthClass
import com.example.web_app_ondemande.ui.theme.OndeMandeCream
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Halaman Katalog Menu Utama Onde Mande (ADAPTIF + RESPONSIF).
 *
 * Fitur Compose:
 * - Scaffold untuk mengelola struktur halaman dan penempatan CartBar di bottomBar.
 * - LazyVerticalGrid + GridCells.Adaptive(320.dp): jumlah kolom menyesuaikan lebar layar
 *   (HP = 1 kolom, tablet portrait = 2 kolom, tablet landscape = 3 kolom) tanpa angka kolom tetap.
 * - currentWidthClass() untuk mengatur jarak tepi sesuai ukuran layar.
 * - widthIn(max = ContentMaxWidth) agar konten tetap rapi di tengah pada layar sangat lebar.
 * - BadgedBox & Badge pada header top bar, derivedStateOf untuk total keranjang.
 *
 * Bagian Tampilan:
 * - Menampilkan daftar 10 hidangan Minang beserta stepper kuantitas, ikon badge keranjang di header,
 *   dan bar keranjang melayang.
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
    val widthClass = currentWidthClass()
    val horizontalPadding = if (widthClass == WindowWidthClass.Compact) 20.dp else 28.dp

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
        // Box di tengah + widthIn: di layar lebar konten tidak melebar sampai ke tepi
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = ContentMaxWidth)
                    .fillMaxSize()
            ) {
                // Header Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 16.dp),
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
                    // Badge diletakkan DI LUAR lingkaran putih (yang di-clip), supaya tidak terpotong
                    Box(modifier = Modifier.size(40.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onNavigateToCart() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🛒",
                                fontSize = 18.sp
                            )
                        }

                        if (totalItems > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-6).dp)
                            ) {
                                Text(
                                    text = totalItems.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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

                // Daftar Katalog Makanan (Grid Adaptif: kolom bertambah mengikuti lebar layar)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 320.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = 8.dp,
                        bottom = 120.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
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
}

@Preview(name = "HP", showBackground = true)
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

@Preview(name = "Tablet Landscape", showBackground = true, widthDp = 1024, heightDp = 700)
@Composable
fun MenuScreenTabletPreview() {
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

@Preview(name = "Tablet Portrait", showBackground = true, widthDp = 700, heightDp = 1000)
@Composable
fun MenuScreenTabletPortraitPreview() {
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