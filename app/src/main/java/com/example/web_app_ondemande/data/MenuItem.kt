package com.example.web_app_ondemande.data

import androidx.annotation.DrawableRes

/**
 * Model data untuk item menu masakan Padang Onde Mande.
 * @param id ID unik menu
 * @param name Nama masakan Minang
 * @param price Harga dalam mata uang Rupiah
 * @param imageRes Resource ID gambar drawable lokal (.webp)
 * @param description Deskripsi singkat hidangan
 */
data class MenuItem(
    val id: Int,
    val name: String,
    val price: Int,
    @param:DrawableRes val imageRes: Int,
    val description: String = ""
)
