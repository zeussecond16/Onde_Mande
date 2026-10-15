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
import com.example.web_app_ondemande.ui.theme.OndeMandeCream
import com.example.web_app_ondemande.ui.theme.OndeMandeDarkBtn
import com.example.web_app_ondemande.ui.theme.OndeMandeInk
import com.example.web_app_ondemande.ui.theme.OndeMandeMuted
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Halaman Utama (Landing Page / Home) Onde Mande.
 *
 * Fitur Compose:
 * - Scaffold dengan containerColor = OndeMandeCream untuk latar belakang krem khas.
 * - Column dengan Modifier.verticalScroll agar seluruh konten utama dapat digulir.
 * - Hero section dengan Serif typography ("Cita Rasa Minang, Hangatnyo Rumah di Satiap Suapan").
 * - Tombol CTA ("Pesan Sekarang ›") untuk bernavigasi ke halaman Menu.
 * - FoodCarousel untuk menampilkan galeri 3D makanan unggulan.
 * - Section Fitur Keunggulan menggunakan Column & Text.
 *
 * Bagian Tampilan:
 * - Menampilkan landing page utama restoran Onde Mande dengan nuansa desain asli.
 */
@Composable
fun HomeScreen(
    onNavigateToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OndeMandeCream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar Navbar (Logo)
            Row(
                modifier = Modifier
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

                // Tombol Menu Bulat Hitam
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OndeMandeInk)
                        .clickable { onNavigateToMenu() },
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

            // Hero Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Carousel Foto Makanan Bergulir
            FoodCarousel(
                items = MenuData.carouselItems,
                onItemClick = { onNavigateToMenu() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section Fitur Keunggulan
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FeatureArticle(
                    title = "Rampah Asli Minang",
                    description = "Bumbu diracik sendiri dengan resep turun-temurun, tanpa pengawet, supaya rasanya tetap otentik."
                )
                FeatureArticle(
                    title = "Panggarah tiok Hari",
                    description = "Rendang, gulai, dan dendeng balado disiapkan setiap pagi, jadi selalu hangat dan baru matang."
                )
                FeatureArticle(
                    title = "Layan Antar & Catering",
                    description = "Makan di tempat, bawa pulang, atau pesan untuk acara keluarga dan kantor."
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    OndeMandeTheme {
        HomeScreen(onNavigateToMenu = {})
    }
}
