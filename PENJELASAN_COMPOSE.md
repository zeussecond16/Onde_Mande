# 📱 PANDUAN PRESENTASI & PENJELASAN JETPACK COMPOSE (ONDE MANDE)

Dokumen ini disusun untuk membantu menjelaskan arsitektur dan penggunaan fitur **Jetpack Compose (Material 3)** pada aplikasi **Onde Mande** dalam sesi presentasi perkuliahan.

---

## 1. TABEL PEMETAAN KOMPONEN & FITUR JETPACK COMPOSE

| Komponen / Fitur Compose | File & Fungsi | Efeknya Pada Tampilan | Cara Menjelaskannya Singkat |
| :--- | :--- | :--- | :--- |
| **`Scaffold` & `containerColor`** | `HomeScreen.kt`, `MenuScreen.kt`, `CartScreen.kt` | Menyediakan tata letak standar aplikasi (TopBar, BottomBar, & warna latar belakang Krem `#FDF8EC`). | *"Scaffold memberi wadah dasar halaman dan otomatis menangani area inset status bar/navigation bar."* |
| **`HorizontalPager` & `rememberPagerState`** | `FoodCarousel.kt` (`FoodCarousel()`) | Memungkinkan slide/swipe foto makanan secara horizontal di halaman utama. | *"HorizontalPager membuat galeri foto bisa di-swipe dengan animasi transisi halaman yang mulus."* |
| **`Modifier.graphicsLayer`** | `FoodCarousel.kt` (`graphicsLayer { ... }`) | Mengatur rotasi Y, skala X/Y, dan transparansi (alpha) kartu makanan secara dinamis. | *"graphicsLayer mengubah rotasi dan skala kartu berdasarkan jarak dari halaman tengah, menciptakan efek 3D melengkung pengganti CSS 3D Carousel."* |
| **`LazyColumn` & `items(key = ...)`** | `MenuScreen.kt`, `CartScreen.kt` | Merender daftar menu makanan secara efisien hanya untuk item yang terlihat di layar. | *"LazyColumn adalah versi Compose dari RecyclerView. Parameter `key` memastikan Compose tidak merender ulang item yang tidak berubah saat scroll."* |
| **`Card` & `CardDefaults.cardElevation`** | `MenuItemCard.kt`, `CartBar.kt` | Menampilkan wadah kartu makanan dengan sudut melengkung `22.dp` dan bayangan elevasi. | *"Card memberi bentuk container putih bersih dengan bayangan lembut agar kartu masakan tampak timbul dari latar belakang."* |
| **`AnimatedVisibility`** | `CartBar.kt` (`AnimatedVisibility`) | Memunculkan dan menyembunyikan Bar Keranjang secara halus (*slide up/down*). | *"AnimatedVisibility otomatis memunculkan bar keranjang melayang hanya ketika pengguna mulai memilih minimal 1 porsi makanan."* |
| **`derivedStateOf`** | `MenuScreen.kt`, `CartScreen.kt` | Menghitung total item dan akumulasi harga Rupiah secara reaktif berdasarkan `cartState`. | *"derivedStateOf mengoptimalkan Recomposition agar perhitungan harga total hanya dieksekusi saat data keranjang benar-benar berubah."* |
| **`rememberSaveable` + Custom `Saver`** | `AppNavHost.kt` (`rememberSaveable`) | Menjaga data porsi keranjang agar tidak hilang saat terjadi rotasi layar HP (*configuration change*). | *"rememberSaveable menyimpan state keranjang ke dalam Bundle sistem sehingga item belanjaan tetap tersimpan saat layar diputar."* |
| **`NavHost` & `composable()`** | `AppNavHost.kt` (`NavHost()`) | Mengelola navigasi antarlayar (`Home` $\rightarrow$ `Menu` $\rightarrow$ `Cart`). | *"NavHost menangani perpindahan antar halaman secara deklaratif tanpa perlu Fragment atau Intent manual."* |
| **`AlertDialog`** | `CartScreen.kt` (`AlertDialog`) | Menampilkan dialog pop-up konfirmasi pesanan setelah tombol "Konfirmasi & Pesan" diklik. | *"AlertDialog menampilkan ringkasan akhir pesanan sebagai simulasi pemicu pemesanan tanpa memerlukan backend server."* |

---

## 2. DIAGRAM ALUR STATE (STATE HOISTING & RECOMPOSITION)

Aplikasi menggunakan pola **State Hoisting** di mana `cartState` dikelola secara terpusat di [AppNavHost.kt](file:///home/ega156/PEMROGRAMAN_MOBILE/Onde_Mande/app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) dan diturunkan ke layar-layar bawah.

```text
               [ USER INTERACTION ]
  (Pengguna menekan tombol + / - pada QuantityStepper)
                        │
                        ▼
               [ EVENT CALLBACK ]
      (onIncrease(id) / onDecrease(id) dipanggil)
                        │
                        ▼
             [ STATE MAP UPDATED ]
      cartState (SnapshotStateMap<Int, Int>) diperbarui
                        │
                        ▼
             [ DERIVED STATE CALC ]
  derivedStateOf menghitung ulang totalItems & totalPrice
                        │
                        ▼
               [ RECOMPOSITION ]
  • MenuItemCard  -> Angka kuantitas berubah
  • CartBar       -> Bar keranjang slide-in / angka total berubah
  • CartScreen    -> Subtotal & daftar item diisi ulang
```

---

## 3. PERBEDAAN ARSITEKTUR: DARI WEBVIEW LAMA KE JETPACK COMPOSE MURNI

| Kriteria | Versi Hybrid WebView Lama | Versi Jetpack Compose Murni Baru |
| :--- | :--- | :--- |
| **Teknologi Utama** | HTML5, CSS3, JavaScript lokal di `assets/` | Kotlin 100%, Jetpack Compose, Material 3 |
| **Rendering Engine** | Android `android.webkit.WebView` | Native Android Compose Rendering (Canvas/Skia) |
| **Performa & Memori** | Membutuhkan memori browser internal (WebKit) | Sangat ringan, cepat, dan teroptimasi native |
| **Navigasi Layar** | Navigasi halaman HTML internal (`home.html` $\leftrightarrow$ `menu.html`) | Navigation Compose (`NavHost` & `rememberNavController`) |
| **Manajemen State** | Variabel JavaScript lokal (`qty = {}`) | Compose State (`remember`, `mutableStateMapOf`, `derivedStateOf`) |
| **Ketahanan Rotasi** | State JS riskan reset saat Activity di-destroy | Terjamin via `rememberSaveable` dan Custom `Saver` |

---

## 4. PETUNJUK PENGUJIAN MANUAL (VERIFIKASI FITUR)

Untuk mendemonstrasikan aplikasi secara manual di emulator atau perangkat Android:

1. **Navigasi & Hero Section (Home)**:
   - Buka aplikasi. Perhatikan judul Serif "*Cita Rasa Minang, Hangatnyo Rumah di Satiap Suapan*".
   - Perhatikan **3D Carousel Foto Makanan** bergulir otomatis secara otomatis setiap 2.8 detik.
   - Lakukan geser (*swipe*) manual pada carousel untuk merasakan efek rotasi & skala 3D.
   - Klik tombol **"Pesan Sekarang ›"** untuk berpindah ke layar Menu.

2. **Katalog Menu & Stepper Kuantitas (Menu)**:
   - Pada halaman Menu, klik tombol `+` pada salah satu masakan (misal: *Ayam Gulai*).
   - Perhatikan **CartBar (Bar Keranjang Melayang)** otomatis muncul (*slide-up*) di bagian bawah layar menampilkan jumlah item dan total harga.
   - Coba tekan tombol `-` hingga 0 porsi untuk memastikan tombol `-` otomatis nonaktif dan CartBar tersembunyi (*slide-down*) jika total item = 0.

3. **Pemeriksaan Ketahanan State (Rotasi Layar HP)**:
   - Tambahkan beberapa item makanan (misal: 2x *Ayam Gulai*, 1x *Cumi Terong*).
   - Putar layar HP (*rotate device*) ke orientasi Landscape lalu kembali ke Portrait.
   - **Hasil Teruji**: Data porsi dan total harga di keranjang tidak hilang karena disimpan oleh `rememberSaveable`.

4. **Ringkasan Keranjang & Konfirmasi Pesanan (Checkout)**:
   - Klik tombol **"Pesan"** pada CartBar untuk masuk ke halaman **Keranjang Pesanan**.
   - Perhatikan rincian item, perkalian harga porsi, subtotal, dan total pembayaran.
   - Klik tombol **"Konfirmasi & Pesan"**. `AlertDialog` konfirmasi pesanan akan muncul menampilkan ringkasan pesanan.
   - Klik tombol **"Selesai"** pada dialog untuk mengosongkan keranjang dan kembali ke katalog menu.
