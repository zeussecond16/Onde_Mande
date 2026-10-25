package com.example.web_app_ondemande.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.ui.adaptive.ContentMaxWidth
import com.example.web_app_ondemande.ui.adaptive.WindowWidthClass
import com.example.web_app_ondemande.ui.adaptive.currentWidthClass
import com.example.web_app_ondemande.ui.theme.OndeMandeCream
import com.example.web_app_ondemande.ui.theme.OndeMandeDarkBtn
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeMuted
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/** Data tiga keunggulan restoran (judul, deskripsi). */
private val featureList = listOf(
    "Rampah Asli Minang" to "Bumbu diracik sendiri dengan resep turun-temurun, tanpa pengawet, supaya rasanya tetap otentik.",
    "Panggarah tiok Hari" to "Rendang, gulai, dan dendeng balado disiapkan setiap pagi, jadi selalu hangat dan baru matang.",
    "Layan Antar & Catering" to "Makan di tempat, bawa pulang, atau pesan untuk acara keluarga dan kantor."
)

/**
 * Halaman Utama (Landing Page / Home) Onde Mande (ADAPTIF + RESPONSIF).
 *
 * Tata letak berubah sesuai lebar layar (currentWidthClass):
 * - Compact (HP)         : satu kolom ke bawah (hero -> carousel -> 3 keunggulan menumpuk).
 * - Medium (tablet kecil): satu kolom, carousel diperbesar, 3 keunggulan berjajar menyamping.
 * - Expanded (tablet lebar): hero di kiri dan carousel di kanan (dua kolom), keunggulan berjajar.
 *
 * Konten dibatasi widthIn(max = ContentMaxWidth) dan diletakkan di tengah agar tetap rapi di layar sangat lebar.
 */
@Composable
fun HomeScreen(
    onNavigateToMenu: () -> Unit,
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val widthClass = currentWidthClass()
    val isWide = widthClass != WindowWidthClass.Compact
    val isExpanded = widthClass == WindowWidthClass.Expanded
    // Di layar lebar lebih banyak kartu terlihat, jadi kartu pinggir dibuat lebih tegak
    val edgeTilt = if (isWide) 38f else 84f

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OndeMandeCream
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = ContentMaxWidth)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar Navbar (Logo + tombol bulat hitam)
                HomeTopBar(onNavigateToCart = onNavigateToCart)

                if (isExpanded) {
                    // TABLET LEBAR: hero di kiri, carousel di kanan
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        HeroSection(
                            onNavigateToMenu = onNavigateToMenu,
                            modifier = Modifier.weight(1f)
                        )
                        FoodCarousel(
                            items = MenuData.carouselItems,
                            onItemClick = { onNavigateToMenu() },
                            cardScale = 1.3f,
                            maxTiltDegrees = edgeTilt,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    // HP & TABLET KECIL: satu kolom
                    HeroSection(onNavigateToMenu = onNavigateToMenu)

                    Spacer(modifier = Modifier.height(16.dp))

                    FoodCarousel(
                        items = MenuData.carouselItems,
                        onItemClick = { onNavigateToMenu() },
                        cardScale = if (isWide) 1.3f else 1f,
                        maxTiltDegrees = edgeTilt
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section Fitur Keunggulan: berjajar di layar lebar, menumpuk di HP
                FeaturesSection(sideBySide = isWide)

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Onde Mande",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Tombol Bulat Hitam (membuka keranjang)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(OndeMandeInk)
                .clickable { onNavigateToCart() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 2.dp)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 2.dp)
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
private fun HeroSection(
    onNavigateToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Cita Rasa Minang,",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Hangatnyo Rumah di Satiap Suapan",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Rendang empuk, gulai kaya rempah, dan sambal lado yang menggoda, dimasak segar setiap hari.",
            style = MaterialTheme.typography.bodyLarge,
            color = OndeMandeMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )

        // Tombol CTA "Pesan Sekarang ›"
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(OndeMandeInk)
                .clickable { onNavigateToMenu() }
                .padding(start = 22.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Pesan Sekarang",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(OndeMandeDarkBtn),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "›",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun FeaturesSection(
    sideBySide: Boolean,
    modifier: Modifier = Modifier
) {
    if (sideBySide) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            featureList.forEach { (title, description) ->
                FeatureArticle(
                    title = title,
                    description = description,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            featureList.forEach { (title, description) ->
                FeatureArticle(title = title, description = description)
            }
        }
    }
}

@Composable
private fun FeatureArticle(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = OndeMandeInk
        )
        Text(
            text = description,
            fontSize = 13.sp,
            color = OndeMandeMuted,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Preview(name = "HP", showBackground = true)
@Composable
fun HomeScreenPreview() {
    OndeMandeTheme {
        HomeScreen(onNavigateToMenu = {}, onNavigateToCart = {})
    }
}

@Preview(name = "Tablet Portrait", showBackground = true, widthDp = 700, heightDp = 1000)
@Composable
fun HomeScreenTabletPortraitPreview() {
    OndeMandeTheme {
        HomeScreen(onNavigateToMenu = {}, onNavigateToCart = {})
    }
}

@Preview(name = "Tablet Landscape", showBackground = true, widthDp = 1024, heightDp = 700)
@Composable
fun HomeScreenTabletPreview() {
    OndeMandeTheme {
        HomeScreen(onNavigateToMenu = {}, onNavigateToCart = {})
    }
}