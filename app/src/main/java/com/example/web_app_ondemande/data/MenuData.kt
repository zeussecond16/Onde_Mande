package com.example.web_app_ondemande.data

import com.example.web_app_ondemande.R
import java.util.Locale

/**
 * Daftar statis menu masakan Minang asli Onde Mande.
 */
object MenuData {
    val items = listOf(
        MenuItem(
            id = 1,
            name = "Ayam Gulai",
            price = 15000,
            imageRes = R.drawable.naspad_ayam_gulai,
            description = "Ayam empuk dimasak gulai santan kaya rempah Minang."
        ),
        MenuItem(
            id = 2,
            name = "Ayam Pop",
            price = 14000,
            imageRes = R.drawable.naspad_ayam_pop,
            description = "Ayam khas Bukittinggi gurih disajikan dengan sambal tomat."
        ),
        MenuItem(
            id = 3,
            name = "Cumi Terong",
            price = 20000,
            imageRes = R.drawable.naspad_cumi_terong,
            description = "Cumi segar dan terong balado cabe merah yang menggugah selera."
        ),
        MenuItem(
            id = 4,
            name = "Dendeng Lambok",
            price = 20000,
            imageRes = R.drawable.naspad_dendeng_lambok,
            description = "Dendeng sapi basah dengan bumbu cabe merah lado segar."
        ),
        MenuItem(
            id = 5,
            name = "Gulai Otak",
            price = 15000,
            imageRes = R.drawable.naspad_gulai_otak,
            description = "Gulai otak sapi lembut dengan kuah kuning rempah istimewa."
        ),
        MenuItem(
            id = 6,
            name = "Gulai Tunjang",
            price = 15000,
            imageRes = R.drawable.naspad_gulai_tunjang,
            description = "Tunjang/kikil sapi kenyal direbus lama dalam kuah gulai pedas."
        ),
        MenuItem(
            id = 7,
            name = "Ikan Bakar",
            price = 17000,
            imageRes = R.drawable.naspad_ikan_kembung_bakar,
            description = "Ikan kembung bakar bumbu khas Minang yang harum dan lezat."
        ),
        MenuItem(
            id = 8,
            name = "Cabe Ijo",
            price = 12000,
            imageRes = R.drawable.naspad_kikil_cabe_hijau,
            description = "Kikil dan cumi masak sambal lado hijau pedas segar."
        ),
        MenuItem(
            id = 9,
            name = "Paruh Goreng",
            price = 15000,
            imageRes = R.drawable.naspad_paruh_goreng,
            description = "Paru sapi garing renyah dibumbui rempah tradisional."
        ),
        MenuItem(
            id = 10,
            name = "Telur Balado",
            price = 13000,
            imageRes = R.drawable.naspad_telur_balado,
            description = "Telur bulat goreng disiram sambal balado merah meriah."
        )
    )

    /**
     * Item unggulan untuk ditampilkan di carousel Home.
     */
    val carouselItems = listOf(
        items[0], // Ayam Gulai
        items[2], // Cumi Terong
        items[5], // Gulai Tunjang
        items[6], // Ikan Bakar
        items[7], // Cabe Ijo
        items[9]  // Telur Balado
    )

    /**
     * Helper untuk format harga mata uang Rupiah.
     */
    fun formatRupiah(price: Int): String {
        val formatted = String.format(Locale.US, "%,d", price).replace(',', '.')
        return "Rp $formatted"
    }
}
