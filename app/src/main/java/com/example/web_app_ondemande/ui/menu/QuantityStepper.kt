package com.example.web_app_ondemande.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeStepperBg
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Komponen Stepper Kuantitas (+ / -) yang dapat digunakan ulang di layar Menu dan Keranjang.
 *
 * Fitur Compose:
 * - Modifier.clip & Modifier.background untuk bentuk kapsul/pill.
 * - IconButton dengan Icon bundar untuk aksi tambah/kurang.
 * - Tombol minus otomatis disabled jika kuantitas == 0.
 *
 * Bagian Tampilan:
 * - Mengatur tombol `- 0 +` di sebelah kanan kartu menu makanan.
 */
@Composable
fun QuantityStepper(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(OndeMandeStepperBg)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Tombol Minus (-)
        IconButton(
            onClick = onDecrease,
            enabled = quantity > 0,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (quantity > 0) Color.White else Color.White.copy(alpha = 0.5f))
        ) {
            Text(
                text = "−",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (quantity > 0) OndeMandeInk else OndeMandeInk.copy(alpha = 0.35f)
            )
        }

        // Angka Kuantitas
        Text(
            text = quantity.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = OndeMandeInk,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Tombol Plus (+)
        IconButton(
            onClick = onIncrease,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(OndeMandeInk)
        ) {
            Text(
                text = "+",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityStepperPreview() {
    OndeMandeTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuantityStepper(quantity = 0, onIncrease = {}, onDecrease = {})
            QuantityStepper(quantity = 2, onIncrease = {}, onDecrease = {})
        }
    }
}
