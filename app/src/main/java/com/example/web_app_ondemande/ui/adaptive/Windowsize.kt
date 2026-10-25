package com.example.web_app_ondemande.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Kelas lebar layar (mengikuti batas resmi Material 3 / Android):
 * - Compact  : < 600dp  -> HP (portrait)
 * - Medium   : 600-839dp -> tablet kecil / HP landscape / tablet portrait
 * - Expanded : >= 840dp  -> tablet landscape / layar besar
 *
 * Dipakai oleh Home, Menu, dan Cart untuk memilih tata letak yang berbeda.
 */
enum class WindowWidthClass { Compact, Medium, Expanded }

/** Lebar maksimum konten di layar besar (supaya tidak melebar sampai tepi layar). */
val ContentMaxWidth = 1100.dp

/** Lebar maksimum untuk tata letak satu kolom (form, daftar, tombol) di layar sedang/besar. */
val SingleColumnMaxWidth = 640.dp

/**
 * Membaca lebar jendela saat ini. Otomatis dihitung ulang kalau layar diputar,
 * jendela diubah ukurannya (multi-window), atau perangkat dilipat/dibuka.
 */
@Composable
fun currentWidthClass(): WindowWidthClass {
    val widthDp = LocalConfiguration.current.screenWidthDp
    return when {
        widthDp < 600 -> WindowWidthClass.Compact
        widthDp < 840 -> WindowWidthClass.Medium
        else -> WindowWidthClass.Expanded
    }
}