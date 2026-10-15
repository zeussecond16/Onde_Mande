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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
 * Komponen Carousel Makanan Bergulir 3D di Halaman Utama.
 *
 * Fitur Compose:
 * - HorizontalPager + rememberPagerState untuk membuat halaman yang bisa di-swipe secara horizontal.
 * - Modifier.graphicsLayer: Mengubah rotasi Y, skala, dan transparansi (alpha) kartu makanan secara dinamis
 *   berdasarkan jarak halaman dari pusat aktif, memberikan efek 3D melengkung pengganti CSS 3D Carousel.
 * - LaunchedEffect(Unit) untuk mengaktifkan pemutaran otomatis (auto-scroll) setiap 2.8 detik.
 *
 * Bagian Tampilan:
 * - Menampilkan 6 foto makanan unggulan yang berputar halus di halaman Home.
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
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 110.dp),
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue

            val menuItem = items[page]

            Card(
                modifier = Modifier
                    .width(150.dp)
                    .height(210.dp)
                    .graphicsLayer {
                        // Efek 3D: Skala, Alpha, dan Rotasi berdasarkan jarak dari pusat
                        val scale = lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        scaleX = scale
                        scaleY = scale

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        // Rotasi Y melengkung mirip efek 3D CSS
                        rotationY = lerp(
                            start = -25f,
                            stop = 25f,
                            fraction = (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).coerceIn(-1f, 1f)
                        )
                    }
                    .clickable { onItemClick(menuItem) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = OndeMandeCardImageBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = menuItem.imageRes),
                    contentDescription = menuItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
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
