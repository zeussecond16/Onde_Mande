package com.example.web_app_ondemande.ui.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
 * Komponen Stepper Kuantitas (+ / -) interaktif yang efisien dan kontras tinggi.
 *
 * Fitur Compose:
 * - AnimatedContent: Mengubah tampilan dari tombol tunggal '+' menjadi stepper lengkap [- angka +] saat kuantitas >= 1.
 * - animateContentSize: Memberikan animasi transisi lebar otomatis saat kuantitas berubah dari 0 ke 1.
 * - MaterialTheme.colorScheme: Menggunakan palet warna tema resmi agar kontras tinggi & tidak tertutup.
 *
 * Bagian Tampilan:
 * - Menghilangkan overlap angka 0, menampilkan tombol '-' berkontras jelas dan angka yang berganti mulus.
 */
@Composable
fun QuantityStepper(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (quantity == 0) {
            // Tampilan Kuantitas 0: Hanya tombol Tambah "+" tunggal yang jelas
            FilledIconButton(
                onClick = onIncrease,
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Tampilan Kuantitas >= 1: Stepper Lengkap [-] [Angka] [+]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Tombol Kurang (-) dengan garis batas kontras tinggi
                OutlinedIconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "−",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Angka Kuantitas dengan animasi pergantian angka halus
                AnimatedContent(
                    targetState = quantity,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { height -> height } + fadeIn() togetherWith
                                    slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            slideInVertically { height -> -height } + fadeIn() togetherWith
                                    slideOutVertically { height -> height } + fadeOut()
                        }
                    },
                    label = "QuantityAnimation"
                ) { targetQuantity ->
                    Text(
                        text = targetQuantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(min = 26.dp)
                    )
                }

                // Tombol Tambah (+)
                FilledIconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "+",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityStepperPreviewZero() {
    OndeMandeTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            QuantityStepper(quantity = 0, onIncrease = {}, onDecrease = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityStepperPreviewThree() {
    OndeMandeTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            QuantityStepper(quantity = 3, onIncrease = {}, onDecrease = {})
        }
    }
}
