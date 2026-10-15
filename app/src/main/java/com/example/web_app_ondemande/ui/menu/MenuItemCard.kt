package com.example.web_app_ondemande.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.data.MenuItem
import com.example.web_app_ondemande.ui.theme.OndeMandeCardImageBg
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Kartu komponen untuk menampilkan setiap item masakan di daftar Menu.
 *
 * Fitur Compose:
 * - Card & CardDefaults untuk permukaan kartu dengan elevasi lembut.
 * - Row & Column terstruktur rapi agar gambar, info, dan stepper berjarak proporsional.
 * - ContentScale.Crop pada gambar makanan agar terisi presisi di kotak 90.dp.
 * - Modifier.weight(1f) pada kolom info agar nama dan harga mengambil sisa ruang dengan fleksibel.
 *
 * Bagian Tampilan:
 * - Menampilkan foto makanan, nama masakan Minang, harga Rupiah, dan tombol Stepper kuantitas yang responsif.
 */
@Composable
fun MenuItemCard(
    item: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Foto Makanan
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(OndeMandeCardImageBg)
            )

            // Informasi Menu (Nama & Harga)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = MenuData.formatRupiah(item.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Stepper Kuantitas
            QuantityStepper(
                quantity = quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemCardPreviewZero() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MenuItemCard(
                item = MenuData.items[0],
                quantity = 0,
                onIncrease = {},
                onDecrease = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemCardPreviewActive() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MenuItemCard(
                item = MenuData.items[0],
                quantity = 2,
                onIncrease = {},
                onDecrease = {}
            )
        }
    }
}
