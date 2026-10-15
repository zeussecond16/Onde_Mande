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
import com.example.web_app_ondemande.ui.theme.OndeMandeMuted
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Baris item pesanan dalam daftar Keranjang / Checkout.
 *
 * Fitur Compose:
 * - Card & Row untuk menampilkan foto, nama, subtotal harga, dan re-use QuantityStepper.
 * - Menggunakan ulang komponen QuantityStepper yang sama seperti di MenuScreen.
 *
 * Bagian Tampilan:
 * - Menampilkan item yang dipilih beserta kalkulasi subtotal harga (jumlah x harga).
 */
@Composable
fun CartItemRow(
    item: MenuItem,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(OndeMandeCardImageBg)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${MenuData.formatRupiah(item.price)} x $quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OndeMandeMuted
                )
                Text(
                    text = MenuData.formatRupiah(item.price * quantity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

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
fun CartItemRowPreview() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CartItemRow(
                item = MenuData.items[0],
                quantity = 2,
                onIncrease = {},
                onDecrease = {}
            )
        }
    }
}
