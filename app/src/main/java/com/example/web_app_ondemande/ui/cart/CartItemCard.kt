package com.example.web_app_ondemande.ui.cart

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
import com.example.web_app_ondemande.ui.menu.QuantityStepper
import com.example.web_app_ondemande.ui.theme.OndeMandeCardImageBg
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme
import com.example.web_app_ondemande.util.formatRupiah

/**
 * Baris kartu item pesanan dalam daftar Keranjang / Checkout.
 *
 * Fitur Compose:
 * - Card dengan sudut membulat 24.dp & CardDefaults.cardElevation untuk tampilan kartu bersih.
 * - Row & Column untuk menyusun foto 72.dp di kiri, info nama/harga di tengah, dan QuantityStepper di kanan.
 * - Menggunakan ulang komponen QuantityStepper yang sama tanpa duplikasi kode.
 *
 * Bagian Tampilan:
 * - Menampilkan item yang dipesan beserta kalkulasi subtotal (harga x porsi) dan tombol pengubah kuantitas.
 */
@Composable
fun CartItemCard(
    item: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
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
            // Gambar makanan 72.dp membulat
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(OndeMandeCardImageBg)
            )

            // Nama, Harga Satuan, dan Subtotal
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${formatRupiah(item.price)} / porsi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = formatRupiah(item.price * quantity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
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
fun CartItemCardPreview() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CartItemCard(
                item = MenuData.items[0],
                quantity = 2,
                onIncrease = {},
                onDecrease = {}
            )
        }
    }
}
