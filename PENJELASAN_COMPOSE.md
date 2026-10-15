# 📱 PANDUAN PRESENTASI & PENJELASAN JETPACK COMPOSE (ONDE MANDE)

Dokumen ini disusun untuk membantu menjelaskan arsitektur dan penggunaan fitur **Jetpack Compose (Material 3)** pada aplikasi **Onde Mande** dalam sesi presentasi perkuliahan.

---

## 1. TABEL PEMETAAN KOMPONEN & FITUR JETPACK COMPOSE

| Komponen / Fitur Compose | File & Fungsi | Efeknya Pada Tampilan | Cara Menjelaskannya Singkat |
| :--- | :--- | :--- | :--- |
| **`Scaffold` & `containerColor`** | `HomeScreen.kt`, `MenuScreen.kt`, `CartScreen.kt` | Menyediakan tata letak standar aplikasi (TopBar, BottomBar, & warna latar belakang Krem `#FDF8EC`). | *"Scaffold memberi wadah dasar halaman dan otomatis menangani area inset status bar/navigation bar."* |
| **`SingleChoiceSegmentedButtonRow`** | `CartScreen.kt` (`SingleChoiceSegmentedButtonRow`) | Menampilkan dua tombol pilihan opsi pengambilan ("Ambil di tempat" & "Antar") bergaya Material 3. | *"SingleChoiceSegmentedButtonRow menyediakan pemilih opsi segmen yang rapi dan responsif."* |
| **`OutlinedTextField` + `rememberSaveable`** | `CartScreen.kt` (`OutlinedTextField`) | Menginput catatan untuk dapur dengan sudut membulat 16.dp, serta `imePadding()` agar input tidak tertutup keyboard. | *"OutlinedTextField menyimpan teks catatan dapur secara aman sehingga teks tidak hilang saat layar diputar."* |
| **`Modifier.animateItem()`** | `CartScreen.kt` (`items(...)`) | Memberikan animasi penghapusan dan pergeseran yang mulus saat porsi item keranjang berkurang ke 0. | *"Modifier.animateItem() membuat baris yang dihapus menghilang dengan animasi, dan baris lain bergeser mulus."* |
| **`AnimatedContent` / `animateIntAsState`** | `OrderSummaryCard.kt`, `QuantityStepper.kt` | Menganimasikan angka kuantitas dan total pembayaran secara vertikal saat nilainya berubah. | *"AnimatedContent memberikan transisi geser vertikal angka yang halus sehingga pergantian harga terasa hidup."* |
| **`AlertDialog`** | `CartScreen.kt` (`AlertDialog`) | Menampilkan dialog pop-up konfirmasi ringkasan pesanan setelah tombol "Pesan Sekarang" diklik. | *"AlertDialog menampilkan rincian pesanan dan catatan sebagai pemicu simulasi pemesanan tanpa backend."* |
| **`Scaffold bottomBar` + `navigationBarsPadding`** | `CartScreen.kt` (`bottomBar`) | Menempatkan tombol pil hitam "Pesan Sekarang" di bagian bawah layar secara presisi di atas navigation bar sistem. | *"bottomBar menempatkan tombol aksi utama di area bawah layar dengan padding aman terhadap navigation bar HP."* |
| **`State Hoisting` Keranjang Lintas Halaman** | `AppNavHost.kt` (`cartState`) | Dideklarasikan di NavHost dan dikirim ke MenuScreen & CartScreen agar data keranjang selalu sinkron di seluruh halaman. | *"State Hoisting memastikan bahwa perubahan porsi di Keranjang langsung tercermin secara real-time di Menu, dan sebaliknya."* |
| **`HorizontalPager` & `pageSpacing`** | `FoodCarousel.kt` (`FoodCarousel()`) | Memungkinkan slide/swipe foto makanan secara horizontal di halaman utama dengan jarak halaman rapi (`12.dp`). | *"HorizontalPager membuat galeri foto dapat di-swipe dengan animasi transisi antarhalaman yang presisi."* |
| **`Modifier.graphicsLayer` & `cameraDistance`** | `FoodCarousel.kt` (`graphicsLayer { ... }`) | Mengatur rotasi Y halus (-12° s/d 12°), skala, alpha, dan perspektif kamera 3D tanpa kliping kasar. | *"graphicsLayer mengubah rotasi dan skala kartu berdasarkan jarak dari halaman tengah, dengan cameraDistance untuk mencegah potongan kasar pada tepi layar."* |
| **`LazyColumn` & `items(key = ...)`** | `MenuScreen.kt`, `CartScreen.kt` | Merender daftar menu makanan secara efisien hanya untuk item yang terlihat di layar. | *"LazyColumn adalah versi Compose dari RecyclerView. Parameter `key` memastikan Compose tidak merender ulang item yang tidak berubah saat scroll."* |
| **`rememberSaveable` + Custom `Saver`** | `AppNavHost.kt` (`rememberSaveable`) | Menjaga data porsi keranjang agar tidak hilang saat terjadi rotasi layar HP (*configuration change*). | *"rememberSaveable menyimpan state keranjang ke dalam Bundle sistem sehingga item belanjaan tetap tersimpan saat layar diputar."* |

---

## 2. DIAGRAM ALUR STATE & PEMESANAN

Aplikasi menggunakan pola **State Hoisting** di mana `cartState` dikelola secara terpusat di [AppNavHost.kt](file:///home/ega156/PEMROGRAMAN_MOBILE/Onde_Mande/app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) dan diturunkan ke seluruh layar.

### A. Alur Perubahan Kuantitas Item
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
  • MenuItemCard / CartItemCard -> Angka kuantitas berubah
  • CartBar                     -> Slide-in / total berubah
  • CartScreen                  -> Subtotal & daftar item diisi ulang
```

### B. Alur Eksekusi Pemesanan (Checkout Flow)
```text
  [ Tekan "Pesan Sekarang" ] ──► [ AlertDialog Konfirmasi ]
                                        │
                         ┌──────────────┴──────────────┐
                         ▼                             ▼
                 [ Tekan "Batal" ]          [ Tekan "Konfirmasi" ]
                         │                             │
                         ▼                             ▼
                 (Tutup dialog saja)       1. cartState.clear() (Kosongkan keranjang)
                                           2. Tampilkan Snackbar "Pesanan diterima!"
                                           3. PopBackStack kembali ke Home
                                           4. Menu & Cart otomatis bersih (Sinkron)
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

1. **Sinkronisasi Lintas Halaman**:
   - Tambah item di Menu (misal: 2x *Ayam Gulai*).
   - Ketuk CartBar atau ikon Keranjang ber-badge di header TopBar untuk membuka halaman **Keranjang**.
   - Ubah jumlah porsi di Keranjang (misal dikurangi jadi 1x *Ayam Gulai*).
   - Tekan tombol Kembali `‹` ke Menu $\rightarrow$ Perhatikan angka di kartu Menu otomatis ikut berubah menjadi `1x` (100% sinkron).

2. **Animasi Hapus Item (`Modifier.animateItem()`)**:
   - Pada halaman Keranjang, tekan tombol `-` pada suatu item hingga porsinya mencapai `0`.
   - Perhatikan baris kartu item tersebut menghilang dengan animasi slide/fade yang mulus dan baris di bawahnya bergeser dengan rapi.

3. **Input Catatan & Opsi Pengambilan**:
   - Pilih opsi pengambilan ("Ambil di tempat" atau "Antar").
   - Ketik pesan di kolom *Catatan untuk Dapur* (misal: *"sambal dipisah"*).
   - Putar layar HP (*rotate device*) $\rightarrow$ Teks catatan dan pilihan opsi tidak hilang karena menggunakan `rememberSaveable`.

4. **Proses Checkout & Snackbar**:
   - Ketuk tombol **"Pesan Sekarang  •  Rp xx.xxx"**.
   - `AlertDialog` konfirmasi akan muncul menampilkan rincian item, total harga, opsi pengiriman, dan catatan dapur.
   - Tekan **"Ya, Pesan Sekarang"**.
   - Keranjang akan otomatis dikosongkan (`cartState.clear()`), Snackbar *"Pesanan diterima, terima kasih!"* akan muncul, dan layar berpindah kembali ke **Home**.
   - Buka kembali layar Menu atau Keranjang $\rightarrow$ Terbukti keranjang telah dalam keadaan kosong.
