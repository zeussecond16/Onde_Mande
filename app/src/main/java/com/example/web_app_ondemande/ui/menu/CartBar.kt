package com.example.web_app_ondemande.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.ui.adaptive.SingleColumnMaxWidth
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Bar keranjang belanja melayang (Floating Cart Bar) di bagian bawah layar Menu.
 *
 * Fitur Compose:
 * - AnimatedVisibility dengan slideInVertically & slideOutVertically untuk efek kemunculan halus saat totalItems > 0.
 * - navigationBarsPadding() agar posisinya berada tepat di atas bilah navigasi sistem tanpa terpotong.
 * - Card melayang dengan RoundedCornerShape(999.dp) (pill shape) & CardDefaults.cardElevation.
 * - Row & Column untuk memisahkan info keranjang (jumlah item & total harga) dan tombol aksi "Pesan ›".
 *
 * Bagian Tampilan:
 * - Menampilkan ringkasan singkat jumlah item dan total harga serta tombol menuju Keranjang/Checkout.
 */
@Composable
fun CartBar(
    totalItems: Int,
    totalPrice: Int,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = totalItems > 0,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center      // di layar lebar bar tetap di tengah
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = SingleColumnMaxWidth)  // dan tidak melebar penuh
                    .fillMaxWidth(),
                shape = RoundedCornerShape(999.dp),
                colors = CardDefaults.cardColors(containerColor = OndeMandeInk),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Info Item & Total Harga
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "$totalItems item",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = MenuData.formatRupiah(totalPrice),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Tombol "Pesan ›"
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White)
                            .clickable { onCartClick() }
                            .padding(start = 18.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Pesan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OndeMandeInk
                        )
                        // Lingkaran ikon panah ›
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(OndeMandeInk),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "›",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartBarPreview() {
    OndeMandeTheme {
        CartBar(
            totalItems = 2,
            totalPrice = 30000,
            onCartClick = {}
        )
    }
}