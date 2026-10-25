# PEMBAGIAN MATERI PRESENTASI APLIKASI ONDE MANDE

## PEMATERI 1: Pendahuluan, Arsitektur Utama, & Landing Page (Home)

### Cakupan Bagian
Dari aplikasi pertama kali dibuka sampai Halaman Utama (`HomeScreen.kt`).

### Fitur & Kode yang Dijelaskan
- **Pengenalan & Arsitektur Aplikasi**:
  - Aplikasi 100% Jetpack Compose Murni (Material 3) tanpa WebView.
  - Single Activity (`MainActivity.kt`) dengan penanganan *edge-to-edge layout*.
  - Tema `OndeMandeTheme`, warna Krem khas (`#FDF8EC`), dan tipografi Serif untuk judul Minang.

- **Halaman Utama (`HomeScreen.kt`)**:
  - Spanduk Hero ("Cita Rasa Minang, Hangatnyo Rumah di Satiap Suapan") dan tombol CTA "Pesan Sekarang ›".
  - Penerapan *Modifier chain* (`.clip`, `.background`, `.clickable`, `.padding`) pada tombol CTA.

- **3D Carousel Foto Makanan (`FoodCarousel.kt`)**:
  - Penggunaan `HorizontalPager`, `graphicsLayer` (rotasi 3D `-12°` s/d `12°` & `cameraDistance = 8 * density`), serta *auto-scroll* 2.8 detik via `LaunchedEffect`.

- **Adaptasi Layar Home (`Windowsize.kt`)**:
  - Penyesuaian tampilan dari 1 kolom di HP menjadi 2 kolom berdampingan pada layar Tablet/Landscape melalui fungsi `currentWidthClass()`.

---

## PEMATERI 2: Katalog Menu, Stepper Kuantitas, & State Hoisting

### Cakupan Bagian
Halaman Katalog Menu (`MenuScreen.kt`) & Pengelolaan State Terpusat.

### Fitur & Kode yang Dijelaskan
- **Halaman Katalog (`MenuScreen.kt`)**:
  - Header TopBar dengan ikon keranjang & indikator badge jumlah item (`BadgedBox` & `Badge`).
  - Efisiensi daftar 10 masakan Minang menggunakan `LazyColumn` / `LazyVerticalGrid` dengan parameter `key = { it.id }`.
  - Tampilan katalog yang otomatis berubah dari daftar 1 kolom di HP menjadi Grid 2 Kolom di Tablet.

- **Komponen Stepper Kuantitas (`QuantityStepper.kt`)**:
  - Menjelaskan tombol porsi `-` `num` `+` yang *stateless*.
  - Animasi perubahan dari tombol `+` tunggal saat porsi 0 menjadi stepper lengkap saat porsi ≥ 1 menggunakan `AnimatedContent` & `animateContentSize`.

- **Bar Keranjang Melayang (`CartBar.kt`) & State Hoisting**:
  - Animasi kemunculan bar melayang via `AnimatedVisibility` dengan `navigationBarsPadding()`.
  - State Hoisting `cartState` terpusat di `AppNavHost.kt` dan optimasi recomposition menggunakan `derivedStateOf`.

---

## PEMATERI 3: Halaman Keranjang (Checkout), Validasi, & Navigasi

### Cakupan Bagian
Halaman Keranjang (`CartScreen.kt`) sampai Selesai Checkout.

### Fitur & Kode yang Dijelaskan
- **Halaman Checkout (`CartScreen.kt`)**:
  - Tampilan keranjang berisi vs. keranjang kosong (`EmptyCartState.kt`).
  - Kartu item pesanan (`CartItemCard.kt`) dengan animasi hapus mulus `Modifier.animateItem()`.
  - Pemilih Opsi Pengambilan (`SingleChoiceSegmentedButtonRow`) & Input Catatan Dapur (`OutlinedTextField` + `imePadding()`).
  - Tampilan *Two-Pane Split Layout* (2 kolom) saat dibuka di Tablet/Landscape.

- **Form Data Pemesan & Validasi Input**:
  - Form `CustomerFormSection.kt` (Nama, Telepon, dan Alamat bersyarat via `AnimatedVisibility`).
  - Fungsi validasi Kotlin murni `Validation.kt` (Format nomor HP Indonesia, dll.).
  - Mekanisme penanganan error (`isError`, `supportingText`) dan *auto-scroll* ke bagian form bermasalah saat tombol Pesan Sekarang diklik.

- **Checkout, Dialog, & Ketahanan State**:
  - Konfirmasi pesanan via `AlertDialog` & kemunculan pesan melayang `SnackbarHost`.
  - Sinkronisasi pengosongan keranjang (`cartState.clear()`) dan navigasi kembali ke Home.
  - Ketahanan data keranjang saat rotasi layar HP menggunakan `rememberSaveable` + `Saver` kustom (`SnapshotStateMapWrapper`).

---

## RINGKASAN URUTAN ALUR PRESENTASI

1. Pembuka
2. Pemateri 1: Home & Carousel
3. Pemateri 2: Menu & Stepper
4. Pemateri 3: Checkout & Validasi
5. Penutup
