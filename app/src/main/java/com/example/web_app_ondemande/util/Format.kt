package com.example.web_app_ondemande.util

import java.util.Locale

/**
 * Helper fungsi utility untuk memformat angka Integer menjadi string mata uang Rupiah.
 * Contoh: 15000 -> "Rp 15.000"
 */
fun formatRupiah(price: Int): String {
    val formatted = String.format(Locale.US, "%,d", price).replace(',', '.')
    return "Rp $formatted"
}
