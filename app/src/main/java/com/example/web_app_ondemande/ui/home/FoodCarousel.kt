package com.example.web_app_ondemande.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.data.MenuItem
import com.example.web_app_ondemande.ui.theme.OndeMandeCardImageBg
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

/**
 * Komponen Carousel Makanan Bergulir 3D Halus di Halaman Utama.
 *
 * Fitur Compose:
 * - HorizontalPager + rememberPagerState: Membuat galeri bergulir horizontal dengan pageSpacing 12.dp.
 * - Modifier.graphicsLayer dengan cameraDistance = 8 * density: Mengatur efek rotasi Y halus (maksimal 12 derajat),
 *   skala, dan transparansi (alpha) tanpa adanya potongan kasar atau distorsi miring berlebihan.
 * - LaunchedEffect: Menjalankan pemutaran otomatis (auto-scroll) setiap 2.8 detik.
 *
 * Bagian Tampilan:
 * - Menampilkan foto makanan unggulan yang berputar mulus dan rapi di halaman Home.
 */
@Composable
fun FoodCarousel(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size }
    )

    val density = LocalDensity.current.density

    // Auto-scroll pelan setiap 2800 ms
    LaunchedEffect(pagerState) {
        while (true) {
            delay(2800)
            if (items.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % items.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 60.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            )
            val absOffset = pageOffset.absoluteValue
            val menuItem = items[page]

            Card(
                modifier = Modifier
                    .width(220.dp)
                    .height(260.dp)
                    .graphicsLayer {
                        // Skala halus dari 0.88f ke 1.0f
                        val scale = lerp(
                            start = 0.88f,
                            stop = 1f,
                            fraction = 1f - absOffset.coerceIn(0f, 1f)
                        )
                        scaleX = scale
                        scaleY = scale

                        // Transparansi dari 0.65f ke 1.0f
                        alpha = lerp(
                            start = 0.65f,
                            stop = 1f,
                            fraction = 1f - absOffset.coerceIn(0f, 1f)
                        )

                        // Rotasi Y halus (maksimal -12 derajat sampai 12 derajat)
                        rotationY = lerp(
                            start = -12f,
                            stop = 12f,
                            fraction = pageOffset.coerceIn(-1f, 1f)
                        )

                        // Mencegah kliping/terpotong kasar dengan jarak kamera berpresisi
                        cameraDistance = 8 * density
                    }
                    .clickable { onItemClick(menuItem) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = OndeMandeCardImageBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = menuItem.imageRes),
                    contentDescription = menuItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodCarouselPreview() {
    OndeMandeTheme {
        FoodCarousel(
            items = MenuData.carouselItems,
            onItemClick = {}
        )
    }
}
