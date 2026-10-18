package com.example.web_app_ondemande.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Tampilan khusus saat Keranjang dalam keadaan kosong.
 *
 * Fitur Compose:
 * - Text emoji indikator visual berukuran 64.sp yang ringan tanpa library tambahan.
 * - Column dengan Alignment.CenterHorizontally agar seluruh teks & tombol rapi di tengah layar.
 * - Button berbahan Pill shape (RoundedCornerShape(999.dp)) sebagai aksi kembali ke Menu.
 *
 * Bagian Tampilan:
 * - Menampilkan pesan bahwa keranjang masih kosong beserta tombol ajakan bertindak menuju katalog menu.
 */
@Composable
fun EmptyCartState(
    onNavigateToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🛒",
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Keranjang diak belum baisi",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Pilih masakan Minang lezat kesukaan Anda di halaman menu.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onNavigateToMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(999.dp),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(48.dp)
        ) {
            Text(
                text = "Lihat Menu Makanan",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyCartStatePreview() {
    OndeMandeTheme {
        EmptyCartState(onNavigateToMenu = {})
    }
}
