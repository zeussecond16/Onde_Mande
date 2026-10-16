# Onde Mande

## 1. Judul dan Ringkasan

Onde Mande adalah aplikasi pemesanan masakan Minang berbasis Android yang dirancang untuk bekerja secara luring (offline) tanpa ketergantungan pada layanan backend server. Seluruh antarmuka pengguna dibangun secara deklaratif menggunakan Jetpack Compose dan Material Design 3. Aplikasi ini mengelola katalog masakan, galeri visual interaktif, serta kalkulasi keranjang belanja secara real-time dalam satu alur arsitektur tunggal yang terintegrasi.

## 2. Fitur

- **Halaman Utama (Home)**: Menampilkan spanduk hero dengan tipografi Serif khas Minang, tombol navigasi pemesanan, carousel foto makanan 3D bergulir otomatis, serta kartu informasi keunggulan restoran.
- **Halaman Katalog (Menu)**: Menyajikan daftar 10 masakan Minang lengkap dengan foto, nama, harga, stepper porsi interaktif, ikon indikator badge pada header, serta bar keranjang melayang (*floating cart bar*) yang muncul otomatis saat terdapat item yang dipilih.
- **Halaman Keranjang (Cart/Checkout)**: Menampilkan rincian daftar pesanan dengan animasi penghapusan item, pemilih opsi pengiriman (*Ambil di tempat* atau *Antar*), kolom catatan khusus dapur, ringkasan kalkulasi total pembayaran, serta dialog konfirmasi pesanan.

## 3. Teknologi

Tabel berikut merinci pustaka, komponen framework, dan versi SDK yang digunakan dalam pembangunan aplikasi Onde Mande:

| Komponen / Pustaka | Teknologi | Versi / Keterangan |
| :--- | :--- | :--- |
| **Bahasa Pemrograman** | Kotlin | `2.2.10` |
| **Build Tool** | Android Gradle Plugin (AGP) | `9.3.3` |
| **UI Framework** | Jetpack Compose BOM | `2026.02.01` |
| **Desain Sistem** | Material Design 3 | `androidx.compose.material3` |
| **Navigasi** | Navigation Compose | `2.8.8` |
| **Activity Integration** | Activity Compose | `1.13.0` |
| **Core Extension** | Core KTX | `1.19.1` |
| **Lifecycle** | Lifecycle Runtime KTX | `2.11.0` |
| **Target SDK** | Android SDK | `37` (Compile SDK `37`) |
| **Minimum SDK** | Android SDK | `24` (Android 7.0 Nougat) |

## 4. Arsitektur

Aplikasi Onde Mande menerapkan pola arsitektur *Unidirectional Data Flow* (UDF) berbasis Jetpack Compose. Struktur aplikasi terbagi secara jelas ke dalam beberapa lapisan tanggung jawab:

- **Entry Point**: [MainActivity.kt](app/src/main/java/com/example/web_app_ondemande/MainActivity.kt) sebagai peluncur tunggal yang mengonfigurasi *edge-to-edge layout* dan memuat tema aplikasi.
- **Navigasi**: [AppNavHost.kt](app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) mengelola deklarasi rute halaman (`Home`, `Menu`, `Cart`) dan siklus navigasi.
- **State Layer**: Pemilik tunggal kebenaran state keranjang (*Single Source of Truth*) dipertahankan di tingkat teratas navigasi menggunakan `rememberSaveable`.
- **Layar (Screen)**: [HomeScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/home/HomeScreen.kt), [MenuScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/MenuScreen.kt), dan [CartScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartScreen.kt) bertindak sebagai kontainer tingkat halaman yang merangkai komponen-komponen UI.
- **Komponen UI Stateless**: Komponen atomik seperti [QuantityStepper.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/QuantityStepper.kt), [MenuItemCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/MenuItemCard.kt), [CartItemCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartItemCard.kt), dan [OrderSummaryCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/OrderSummaryCard.kt) yang murni menerima data dan memicu callback event.
- **Data Layer**: [MenuItem.kt](app/src/main/java/com/example/web_app_ondemande/data/MenuItem.kt) dan [MenuData.kt](app/src/main/java/com/example/web_app_ondemande/data/MenuData.kt) menyediakan struktur data dan sumber katalog statis.
- **Tema**: [Theme.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Theme.kt), [Color.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Color.kt), dan [Type.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Type.kt) mendefinisikan warna, tipografi, dan gaya visual Material 3 secara terpusat.

### Diagram Aliran Data dan State

```text
                  [ Aksi Pengguna (Klik / Input) ]
                               │
                               ▼
               [ Callback Event (onIncrease / onDecrease) ]
                               │
                               ▼
              [ Pembaruan State di AppNavHost ]
          cartState (SnapshotStateMap<Int, Int>)
                               │
                               ▼
            [ Kalkulasi Reaktif via derivedStateOf ]
              totalItems  <───  totalPrice
                               │
                               ▼
            [ Recomposition Komponen UI Terkait ]
    HomeScreen  │  MenuScreen / CartBar  │  CartScreen
```

### Prinsip Utama

1. **Composable Berukuran Kecil**: Setiap komponen dikembangkan dengan tanggung jawab tunggal (*Single Responsibility Principle*).
2. **Komponen Stateless**: Komponen UI tidak menyimpan atau mengubah state internal secara langsung, melainkan menerima data melalui parameter dan meneruskan aksi pengguna melalui lambda event.
3. **State Hoisting**: State dinaikkan ke tingkat [AppNavHost.kt](app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) agar dapat diakses dan dibagikan secara konsisten oleh seluruh layar.
4. **Satu Sumber Kebenaran**: Data kuantitas pesanan disimpan hanya dalam satu peta state (`cartState`), sehingga perubahan di satu halaman langsung berdampak ke halaman lain secara simultan.

## 5. Struktur Proyek

Berikut adalah pohon direktori utama modul aplikasi beserta fungsi dari setiap file:

```text
app/src/main/java/com/example/web_app_ondemande/
├── MainActivity.kt                      # Activity utama peluncur aplikasi
├── data/
│   ├── MenuItem.kt                      # Model data class untuk entitas menu makanan
│   └── MenuData.kt                      # Objek penyedia daftar menu statis dan koleksi carousel
├── navigation/
│   └── AppNavHost.kt                    # Deklarasi rute NavHost dan pengelola state terpusat
├── ui/
│   ├── cart/
│   │   ├── CartItemCard.kt              # Komponen baris kartu pesanan di keranjang
│   │   ├── CartScreen.kt                # Layar checkout, opsi pengiriman, dan konfirmasi pesanan
│   │   ├── EmptyCartState.kt            # Tampilan visual saat keranjang kosong
│   │   └── OrderSummaryCard.kt          # Kartu ringkasan kalkulasi total pembayaran
│   ├── home/
│   │   ├── FoodCarousel.kt              # Carousel foto makanan 3D bergulir horizontal
│   │   └── HomeScreen.kt                # Layar utama landing page
│   ├── menu/
│   │   ├── CartBar.kt                   # Bar melayang indikator keranjang belanja
│   │   ├── MenuItemCard.kt              # Kartu item makanan pada daftar katalog
│   │   ├── MenuScreen.kt                # Layar katalog menu utama
│   │   └── QuantityStepper.kt           # Komponen pengatur kuantitas porsi (+ / -)
│   └── theme/
│       ├── Color.kt                     # Definisi palet warna resmi Onde Mande
│       ├── Theme.kt                     # Setup MaterialTheme dengan LightColorScheme
│       └── Type.kt                      # Konfigurasi gaya tipografi Serif dan Sans-Serif
└── util/
    └── Format.kt                        # Fungsi utilitas format mata uang Rupiah
```

## 6. Penjelasan Modul

### Modul Tema

- **Tanggung Jawab**: Mendefinisikan gaya visual, skema warna, dan tipografi aplikasi yang selaras dengan tema restoran Onde Mande.
- **Komposisi**: [Theme.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Theme.kt) membungkus seluruh hierarki UI dengan fungsi composable `OndeMandeTheme` yang mengaplikasikan `LightColorScheme` dan `Typography`.
- **State**: Statis, tidak mengonsumsi state dinamis. Warna dinamis (*dynamic color*) dimatikan untuk menjamin konsistensi warna di seluruh versi Android.
- **Efek Tampilan**: Memberikan latar belakang krem khas (`#FDF8EC`), warna teks charcoal (`#141414`), serta judul bertipografi Serif.

> **Catatan Teknis: MaterialTheme, ColorScheme, dan Typography**
>
> Pengaturan tema pada Compose dilakukan menggunakan `MaterialTheme` dari Material Design 3. Warna dikelompokkan ke dalam peran semantik seperti `primary`, `background`, `surface`, dan `secondary`. Tipografi dikonfigurasi menggunakan `Typography` dengan pemetaan `FontFamily.Serif` untuk judul berkesan tradisional Minang dan `FontFamily.Default` untuk isi teks.
>
> ```kotlin
> @Composable
> fun OndeMandeTheme(content: @Composable () -> Unit) {
>     MaterialTheme(
>         colorScheme = LightColorScheme,
>         typography = Typography,
>         content = content
>     )
> }
> ```

### Modul Data

- **Tanggung Jawab**: Menyediakan struktur data [MenuItem.kt](app/src/main/java/com/example/web_app_ondemande/data/MenuItem.kt) dan himpunan data statis [MenuData.kt](app/src/main/java/com/example/web_app_ondemande/data/MenuData.kt) untuk 10 hidangan Minang.
- **Komposisi**: `MenuData` menyediakan properti `items` (daftar seluruh menu) dan `carouselItems` (6 menu unggulan).
- **State**: Data imun (*immutable*) berbasis `listOf()`.

### Modul Navigasi

- **Tanggung Jawab**: Mengelola rute perjalanan antarlayar dan memegang state keranjang utama aplikasi.
- **Komposisi**: [AppNavHost.kt](app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) memanggil `NavHost` dengan rute `Screen.Home`, `Screen.Menu`, dan `Screen.Cart`.
- **State**: Memegang `cartState` berupa `SnapshotStateMap<Int, Int>` yang dibungkus dengan `rememberSaveable` dan `Saver` kustom.
- **Efek Tampilan**: Berpindah layar secara mulus dengan penanganan tombol kembali sistem (*system back button*) yang tepat.

> **Catatan Teknis: Navigation Compose dan rememberSaveable dengan Custom Saver**
>
> Navigasi dideklarasikan secara deklaratif menggunakan `NavHost` dan `rememberNavController()`. Agar peta state keranjang (`SnapshotStateMap`) tidak hilang saat terjadi pergantian konfigurasi seperti rotasi layar, state dibungkus menggunakan `rememberSaveable` yang dilengkapi `Saver` kustom. `Saver` ini mengonversi entri peta menjadi `ArrayList<String>` berformat `"id:kuantitas"` saat disimpan dan merestorasinya kembali menjadi `SnapshotStateMap` saat pemulihan.
>
> ```kotlin
> val cartMapSaver = Saver<SnapshotStateMapWrapper, ArrayList<String>>(
>     save = { wrapper ->
>         ArrayList(wrapper.map.entries.map { "${it.key}:${it.value}" })
>     },
>     restore = { list ->
>         val newMap = mutableStateMapOf<Int, Int>()
>         list.forEach { item ->
>             val parts = item.split(":")
>             if (parts.size == 2) newMap[parts[0].toInt()] = parts[1].toInt()
>         }
>         SnapshotStateMapWrapper(newMap)
>     }
> )
> ```

### Modul Home

- **Tanggung Jawab**: Menampilkan spanduk *landing page* dan carousel galeri 3D makanan unggulan.
- **Komposisi**: [HomeScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/home/HomeScreen.kt) memuat struktur `Scaffold`, header navbar, spanduk hero, tombol CTA, [FoodCarousel.kt](app/src/main/java/com/example/web_app_ondemande/ui/home/FoodCarousel.kt), dan daftar artikel keunggulan.
- **State**: `pagerState` dalam `FoodCarousel` yang diatur melalui `rememberPagerState`.
- **Efek Tampilan**: Galeri foto yang bergulir otomatis setiap 2.8 detik dengan efek rotasi perspektif 3D yang halus.

> **Catatan Teknis: HorizontalPager, graphicsLayer, dan cameraDistance**
>
> Penggeseran gambar secara horizontal diimplementasikan menggunakan `HorizontalPager`. Untuk menciptakan efek visual 3D tanpa kliping atau terpotong secara kasar pada tepi layar, digunakan `Modifier.graphicsLayer`. Nilai `rotationY` dihitung secara proporsional dari offset halaman (`-12°` hingga `12°`), dan `cameraDistance` dikonfigurasi menggunakan nilai `8 * density` untuk memperjauh jarak pandang kamera proyeksi 3D.
>
> ```kotlin
> Card(
>     modifier = Modifier
>         .width(220.dp)
>         .height(260.dp)
>         .graphicsLayer {
>             val scale = lerp(0.88f, 1f, 1f - absOffset.coerceIn(0f, 1f))
>             scaleX = scale
>             scaleY = scale
>             alpha = lerp(0.65f, 1f, 1f - absOffset.coerceIn(0f, 1f))
>             rotationY = lerp(-12f, 12f, pageOffset.coerceIn(-1f, 1f))
>             cameraDistance = 8 * density
>         }
> )
> ```

### Modul Menu

- **Tanggung Jawab**: Menampilkan daftar katalog hidangan masakan Minang beserta pengatur kuantitas porsi dan indikator keranjang.
- **Komposisi**: [MenuScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/MenuScreen.kt) merangkai `Scaffold`, header top bar dengan `BadgedBox` & `Badge`, `LazyColumn` yang berisi [MenuItemCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/MenuItemCard.kt), [QuantityStepper.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/QuantityStepper.kt), serta [CartBar.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/CartBar.kt).
- **State**: Menerima `cartState` dari modul navigasi. Total item dan total harga dihitung menggunakan `derivedStateOf`.
- **Efek Tampilan**: Daftar makanan berulir cepat dengan bar keranjang melayang yang muncul secara otomatis (*slide-in*) saat keranjang berisi item.

> **Catatan Teknis: LazyColumn dengan Key, derivedStateOf, dan AnimatedVisibility**
>
> `LazyColumn` digunakan untuk merender daftar item secara efisien. Parameter `key = { it.id }` diberikan pada fungsi `items` untuk memberikan identitas unik pada setiap item sehingga Compose tidak merender ulang elemen yang tidak mengalami perubahan.
>
> Perhitungan akumulasi total item dan total harga dibungkus dalam `derivedStateOf` untuk mengoptimalkan recomposition, sehingga kalkulasi matematika hanya dijalankan ketika isi `cartState` mengalami perubahan.
>
> `CartBar` memanfaatkan `AnimatedVisibility` dengan `slideInVertically` dan `slideOutVertically` untuk memberikan animasi kemunculan dan penyembunyian bar yang mulus saat total item berfluktuasi dari atau ke nol.
>
> ```kotlin
> val totalItems by remember(cartState) {
>     derivedStateOf { cartState.values.sum() }
> }
>
> AnimatedVisibility(
>     visible = totalItems > 0,
>     enter = slideInVertically(initialOffsetY = { it }),
>     exit = slideOutVertically(targetOffsetY = { it })
> ) {
>     // Komponen CartBar
> }
> ```

> **Catatan Teknis: QuantityStepper dengan AnimatedContent dan animateContentSize**
>
> Komponen [QuantityStepper.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/QuantityStepper.kt) bersifat stateless. Ketika kuantitas bernilai 0, hanya satu tombol `FilledIconButton` bertanda `+` yang ditampilkan. Ketika kuantitas bernilai $\ge 1$, tampilan bertransisi menjadi format penuh `[-] kuantitas [+]`.
>
> Transisi perubahan bentuk wadah dianimasikan secara otomatis menggunakan `Modifier.animateContentSize()`. Perubahan angka kuantitas dianimasikan secara vertikal menggunakan `AnimatedContent` dengan transisi `slideInVertically` dan `fadeIn`.
>
> ```kotlin
> Row(
>     modifier = modifier.animateContentSize(),
>     verticalAlignment = Alignment.CenterVertically
> ) {
>     if (quantity == 0) {
>         FilledIconButton(onClick = onIncrease) { Text("+") }
>     } else {
>         OutlinedIconButton(onClick = onDecrease) { Text("−") }
>         AnimatedContent(targetState = quantity) { count ->
>             Text(text = count.toString(), modifier = Modifier.widthIn(min = 26.dp))
>         }
>         FilledIconButton(onClick = onIncrease) { Text("+") }
>     }
> }
> ```

### Modul Keranjang

- **Tanggung Jawab**: Menampilkan rincian pesanan, pemilih opsi pengiriman, kolom catatan dapur, kalkulasi total, serta konfirmasi checkout.
- **Komposisi**: [CartScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartScreen.kt) merangkai `Scaffold`, `SnackbarHost`, `LazyColumn` yang berisi [CartItemCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartItemCard.kt), `SingleChoiceSegmentedButtonRow`, `OutlinedTextField`, [OrderSummaryCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/OrderSummaryCard.kt), [EmptyCartState.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/EmptyCartState.kt), dan `AlertDialog`.
- **State**:
  - `selectedDeliveryOption`: `mutableIntStateOf(0)` dibungkus `rememberSaveable` untuk memilih opsi *Ambil di tempat* atau *Antar*.
  - `kitchenNotes`: `mutableStateOf("")` dibungkus `rememberSaveable` untuk menyimpan masukan catatan dapur.
  - `showConfirmationDialog`: `mutableStateOf(false)` untuk mengontrol keterlihatan dialog konfirmasi.
- **Efek Tampilan**: Pengurangan kuantitas item hingga nol akan menghapus baris kartu secara animatif melalui `Modifier.animateItem()`. Pengetikan pada kolom catatan tidak tertutup keyboard berkat `imePadding()`.

> **Catatan Teknis: animateItem, SingleChoiceSegmentedButtonRow, dan OutlinedTextField dengan imePadding**
>
> Penghapusan item dari daftar pesanan di dalam `LazyColumn` menggunakan `Modifier.animateItem()`. Modifier ini secara otomatis menerapkan animasi *fade* dan pergeseran posisi pada baris lain ketika suatu item dihapus dari peta data.
>
> Pemilihan opsi pengiriman memanfaatkan komponen Material Design 3 `SingleChoiceSegmentedButtonRow` dan `SegmentedButton`.
>
> Penginputan catatan dapur menggunakan `OutlinedTextField` yang dipadukan dengan `Modifier.imePadding()` pada kontainer utama. Modifier ini menyesuaikan *padding* secara dinamis sesuai tinggi *Software Keyboard* (IME) yang muncul, mencegah kolom input tertutup saat pengguna mengetik.
>
> ```kotlin
> LazyColumn {
>     items(items = cartItems, key = { it.id }) { item ->
>         CartItemCard(
>             item = item,
>             quantity = cartState[item.id] ?: 0,
>             modifier = Modifier.animateItem()
>         )
>     }
> }
> ```

### Modul Utilitas

- **Tanggung Jawab**: Menyediakan fungsi utilitas pembantu [Format.kt](app/src/main/java/com/example/web_app_ondemande/util/Format.kt) untuk pemformatan angka bulat (*Integer*) menjadi format string mata uang Rupiah standar (contoh: `15000` $\rightarrow$ `"Rp 15.000"`).

## 7. Manajemen State

Manajemen state pada aplikasi Onde Mande menerapkan prinsip *State Hoisting* murni tanpa penggunaan ViewModel tambahan, sehingga alur data dapat ditelusuri dengan sangat jelas.

### Pemilik dan Pembaca State

- **Pemilik State (`cartState`)**: [AppNavHost.kt](app/src/main/java/com/example/web_app_ondemande/navigation/AppNavHost.kt) menginisialisasi dan menyimpan `SnapshotStateMap<Int, Int>` menggunakan `rememberSaveable`.
- **Pembaca State**:
  - [MenuScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/menu/MenuScreen.kt): Membaca kuantitas porsi setiap menu dan menghitung total harga untuk ditampilkan di `CartBar` serta indikator `Badge`.
  - [CartScreen.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartScreen.kt): Membaca item yang memiliki kuantitas $> 0$ untuk disusun ke dalam baris [CartItemCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/CartItemCard.kt) dan kalkulasi [OrderSummaryCard.kt](app/src/main/java/com/example/web_app_ondemande/ui/cart/OrderSummaryCard.kt).
- **Pengubah State**:
  - Callback `onIncrease`: Menambah kuantitas `cartState[id] = current + 1`.
  - Callback `onDecrease`: Mengurangi kuantitas `cartState[id] = current - 1`, atau menghapus kunci dari peta jika kuantitas mencapai 0.
  - Callback `onConfirmOrder`: Memanggil `cartState.clear()` untuk mengosongkan seluruh isi keranjang belanja secara sekaligus.

### Diagram Alur Siklus Hidup State

```text
               [ Inisialisasi State di AppNavHost ]
      cartState = rememberSaveable(saver = cartMapSaver)
                               │
            ┌──────────────────┴──────────────────┐
            ▼                                     ▼
   [ Diteruskan ke MenuScreen ]          [ Diteruskan ke CartScreen ]
   - Render Kuantitas Menu               - Render Baris Pesanan
   - Render CartBar Floating             - Render Total Pembayaran
            │                                     │
            └──────────────────┬──────────────────┘
                               ▼
            [ Aksi Pengguna (Klik Stepper / Checkout) ]
                               │
                               ▼
            [ Eksekusi Event Callback ke AppNavHost ]
   cartState[id] diperbarui / cartState.clear() dipanggil
                               │
                               ▼
        [ Recomposition Sinkron di Seluruh Layar Aktif ]
```

### Ketahanan State Saat Rotasi Layar

Saat terjadi perubahan konfigurasi perangkat (seperti rotasi layar dari Portrait ke Landscape), Android akan menghancurkan dan membuat ulang `Activity`. Karena `cartState` disimpan menggunakan `rememberSaveable` dengan `Saver` kustom yang merekam seluruh pasangan kunci-nilai ke dalam `Bundle` sistem, isi keranjang belanja tetap pulih secara sempurna tanpa kehilangan data.

## 8. Tema dan Desain

Sistem desain Onde Mande mengacu pada Material Design 3 dengan kustomisasi warna dan tipografi yang mencerminkan identitas kuliner tradisional Minang.

### Palet Warna

Tabel berikut merinci palet warna yang didefinisikan dalam [Color.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Color.kt):

| Nama Variabel Color | Nilai Hex | Peran Semantik dalam Aplikasi |
| :--- | :--- | :--- |
| `OndeMandeCream` | `#FDF8EC` | Warna latar belakang utama seluruh halaman (`background`) |
| `OndeMandeInk` | `#141414` | Warna teks utama, tombol primer, dan bar keranjang (`primary`) |
| `OndeMandeMuted` | `#6B665C` | Warna teks sekunder, harga, dan deskripsi (`secondary`) |
| `OndeMandeStepperBg` | `#F3ECDA` | Latar belakang wadah pengatur kuantitas porsi |
| `OndeMandeCardImageBg` | `#CFC8B8` | Latar belakang tempat foto masakan |
| `OndeMandeSurface` | `#FFFFFF` | Warna latar belakang kartu putih (`surface`) |
| `OndeMandeDarkBtn` | `#3A3A3A` | Latar belakang tombol lingkaran aksen |

### Tipografi

Tipografi dikonfigurasi dalam [Type.kt](app/src/main/java/com/example/web_app_ondemande/ui/theme/Type.kt) dengan menggabungkan dua rumpun font:

- **Font Serif (`FontFamily.Serif`)**: Digunakan untuk judul utama (`headlineLarge`) guna memberikan nuansa tradisional dan elegan pada spanduk tulisan khas seperti *"Cita Rasa Minang,"* dan *"Pilih Hidangan,"*.
- **Font Sans-Serif (`FontFamily.Default`)**: Digunakan untuk judul sekunder, nama masakan, teks harga, dan tombol aksi guna menjamin keterbacaan yang jelas pada layar smartphone.

### Bentuk dan Layout

- **Radius Kartu**: Kartu menu dan kartu pesanan menggunakan sudut melengkung besar `22.dp` hingga `24.dp` dengan latar belakang putih bersih di atas kanvas krem.
- **Bentuk Pill (999.dp)**: Digunakan pada tombol aksi CTA, bar keranjang melayang, dan tombol konfirmasi checkout.
- **Prinsip Spasi**: Menggunakan sistem kisi berorientasi kelipatan `4.dp` dan `8.dp` (`8.dp`, `12.dp`, `16.dp`, `20.dp`, `24.dp`) untuk menjaga konsistensi jarak antar-elemen UI.

## 9. Aset

 Seluruh aset visual gambar masakan disimpan dalam direktori drawable lokal:

- **Lokasi**: `app/src/main/res/drawable/`
- **Konvensi Penamaan**: Menggunakan format nama *snake_case* huruf kecil tanpa spasi (contoh: `naspad_ayam_gulai.webp`, `naspad_cumi_terong.webp`).
- **Format dan Kompresi**: Berformat WebP terkompresi dengan ukuran file berkisar antara 13 KB hingga 16 KB per gambar, menjamin waktu pemuatan gambar yang instan tanpa membebani memori kerja RAM.

## 10. Build dan Menjalankan

### Prasyarat

- **Android Studio**: Android Studio Ladybug (2024.2.1) atau versi yang lebih baru.
- **JDK**: Java Development Kit versi 11 atau yang lebih baru.
- **Android SDK**: Android SDK Platform API Level 37.

### Perintah Build

Untuk mengompilasi aplikasi dan menghasilkan file APK debug melalui Gradle Wrapper, jalankan perintah berikut pada terminal di direktori root proyek:

```bash
./gradlew assembleDebug
```

File APK hasil kompilasi akan dihasilkan di direktori `app/build/outputs/apk/debug/app-debug.apk`.

## 11. Lampiran A - Catatan Migrasi

Tabel berikut merangkai ringkasan migrasi arsitektur dari versi hybrid awal berbasis WebView ke versi Jetpack Compose Murni:

| Komponen Arsitektur | Versi Hybrid WebView Awal | Versi Jetpack Compose Murni Saat Ini |
| :--- | :--- | :--- |
| **Wadah Utama UI** | `android.webkit.WebView` dalam `AndroidView` | Jetpack Compose Native Composables (`Scaffold`, `NavHost`) |
| **Tampilan Halaman** | File HTML/CSS lokal (`home.html`, `menu.html`) | Fungsi Composable Kotlin (`HomeScreen`, `MenuScreen`, `CartScreen`) |
| **Penyimpanan State** | Objek variabel JavaScript (`qty = {}`) | `SnapshotStateMap` Kotlin dibungkus `rememberSaveable` |
| **Navigasi** | Perpindahan tautan URL internal HTML | Navigation Compose (`rememberNavController`, `NavHost`) |
| **Aset Web** | Folder `app/src/main/assets/` (HTML/CSS/JS) | **Dihapus total**; gambar dipindahkan ke `res/drawable/` |
