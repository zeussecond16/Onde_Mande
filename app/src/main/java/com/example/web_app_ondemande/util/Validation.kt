package com.example.web_app_ondemande.util

/**
 * Objek penampung fungsi validasi murni tanpa ketergantungan pada Compose UI.
 */
object Validation {

    /**
     * Validasi Nama Lengkap: Wajib diisi, minimal 2 karakter setelah trim.
     */
    fun validateName(name: String): String? {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return "Nama wajib diisi"
        if (trimmed.length < 2) return "Nama minimal 2 karakter"
        return null
    }

    /**
     * Validasi Nomor Telepon Indonesia:
     * - Diawali 08, 62, atau +62
     * - Total 10-14 digit angka
     */
    fun validatePhone(phone: String): String? {
        val trimmed = phone.trim()
        if (trimmed.isEmpty()) return "Nomor telepon wajib diisi"

        // Format Indonesia: 08xx, 62xx, atau +62xx
        val isIndonesianFormat = trimmed.startsWith("08") ||
                trimmed.startsWith("62") ||
                trimmed.startsWith("+62")
        if (!isIndonesianFormat) {
            return "Nomor telepon harus diawali 08, 62, atau +62"
        }

        // Hitung total digit (abaikan '+')
        val digitCount = trimmed.filter { it.isDigit() }.length
        if (digitCount < 10 || digitCount > 14) {
            return "Nomor telepon harus 10-14 digit"
        }

        return null
    }

    /**
     * Validasi Alamat Pengantaran (Hanya jika opsi "Antar" dipilih).
     * Wajib jika required == true, minimal 10 karakter setelah trim.
     */
    fun validateAddress(address: String, required: Boolean): String? {
        if (!required) return null
        val trimmed = address.trim()
        if (trimmed.isEmpty()) return "Alamat wajib diisi untuk layanan antar"
        if (trimmed.length < 10) return "Alamat pengantaran minimal 10 karakter"
        return null
    }
}
