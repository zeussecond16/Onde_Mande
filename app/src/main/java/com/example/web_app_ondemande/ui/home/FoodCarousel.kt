package com.example.web_app_ondemande.ui.home

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.web_app_ondemande.data.MenuData
import com.example.web_app_ondemande.data.MenuItem
import com.example.web_app_ondemande.ui.theme.OndeMandeCardImageBg
import com.example.web_app_ondemande.ui.theme.OndeMandeCream
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// ====== PENGATURAN KARTU (angka sama dengan versi HTML/CSS awal) ======
private val CARD_WIDTH = 150.dp         // lebar kartu
private val CARD_HEIGHT = 210.dp        // tinggi kartu
private val CARD_STEP = 120.dp          // jarak antar kartu
private val CARD_RADIUS = 20.dp         // lengkung sudut
private const val TILT_DEGREES = 28f    // sudut putar per kartu
private const val SCALE_DROP = 0.08f    // kartu mengecil per langkah dari tengah
private const val FADE_PER_STEP = 0.12f // kartu memudar per langkah dari tengah
private const val DEPTH_DP = 90f        // seberapa jauh kartu samping "mundur" ke belakang
private const val PERSPECTIVE_DP = 900f // kekuatan perspektif 3D
private const val AUTO_SCROLL_MS = 2800L
private const val SCROLL_ANIM_MS = 700

// ====== PENGATURAN BAYANGAN (setara CSS: 0 12px 30px rgba(0,0,0,.15)) ======
private val SHADOW_BLUR = 30.dp         // seberapa menyebar / lembut
private val SHADOW_OFFSET_Y = 12.dp     // seberapa turun ke bawah
private const val SHADOW_ALPHA = 0.12f  // gelap-terang (0.05 = sangat tipis, 0.2 = tegas)

/**
 * Carousel foto bergulir melengkung (desain awal), ringan dan looping tanpa ujung.
 *
 * - Looping: jumlah halaman Int.MAX_VALUE + foto dipilih dengan `page % items.size`.
 * - Kartu menumpuk: ukuran halaman (CARD_STEP) lebih kecil dari lebar kartu (CARD_WIDTH).
 * - Efek 3D: perspektif dihitung manual (Compose tidak punya translateZ seperti CSS).
 * - Bayangan: satu bentuk yang di-blur (BlurMaskFilter), seperti box-shadow di CSS,
 *   jadi tipis dan lembut, bukan tumpukan kotak.
 * - Semua posisi dibaca di dalam graphicsLayer / draw, jadi tidak ada recomposition per frame.
 */
@Composable
fun FoodCarousel(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit,
    cardScale: Float = 1f,          // 1f = ukuran HP, >1f = lebih besar untuk tablet
    maxTiltDegrees: Float = TILT_DEGREES * 3f, // batas miring kartu paling pinggir (makin kecil = pinggir makin tegak)
    modifier: Modifier = Modifier
) {
    // Paint bayangan dibuat sekali saja (tidak dibuat ulang tiap frame)
    val density = LocalDensity.current
    val shadowPaint = remember(density) {
        Paint().apply {
            isAntiAlias = true
            color = Color.Black.copy(alpha = SHADOW_ALPHA).toArgb()
            // radius BlurMaskFilter ~ 0.85 x blur CSS supaya hasilnya setara
            val radiusPx = with(density) { SHADOW_BLUR.toPx() } * 0.85f
            maskFilter = BlurMaskFilter(radiusPx, BlurMaskFilter.Blur.NORMAL)
        }
    }

    if (items.isEmpty()) return

    // Ukuran kartu mengikuti cardScale (HP = 1f, tablet = lebih besar)
    val cardWidth = CARD_WIDTH * cardScale
    val cardHeight = CARD_HEIGHT * cardScale
    val cardStep = CARD_STEP * cardScale
    val cardRadius = CARD_RADIUS * cardScale

    val count = items.size
    val startPage = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % count)
    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { Int.MAX_VALUE }
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(AUTO_SCROLL_MS)
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = SCROLL_ANIM_MS, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight + 90.dp * cardScale),   // ruang ekstra supaya bayangan tidak terpotong
        contentAlignment = Alignment.Center
    ) {
        val sidePadding = (maxWidth - cardStep) / 2

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(cardStep),
            contentPadding = PaddingValues(horizontal = sidePadding),
            pageSpacing = 0.dp,
            beyondViewportPageCount = 3,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val menuItem = items[page % count]

            // Jarak kartu dari tengah: 0 = tengah, + = kanan, - = kiri (dibaca saat menggambar saja)
            val offsetOf = { (page - pagerState.currentPage) - pagerState.currentPageOffsetFraction }

            // Urutan tumpukan hanya berubah saat kartu aktif ganti
            val distance by remember { derivedStateOf { (page - pagerState.currentPage).absoluteValue } }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f - distance),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(cardWidth, cardHeight)
                        .graphicsLayer {
                            val d = offsetOf()
                            val a = d.absoluteValue.coerceAtMost(3f)

                            // Perspektif ala CSS: makin jauh dari tengah, makin kecil & makin dekat ke tengah
                            val factor = PERSPECTIVE_DP / (PERSPECTIVE_DP + a * DEPTH_DP)
                            val s = ((1f - a * SCALE_DROP) * factor).coerceAtLeast(0.3f)
                            scaleX = s
                            scaleY = s
                            translationX = d * cardStep.toPx() * (factor - 1f)

                            // Kartu pinggir dibatasi miringnya (maxTiltDegrees) supaya tidak terlalu pipih.
                            // Jika arah miringnya terbalik di HP kamu, tambah tanda minus sebelum (d.coerceIn ...)
                            rotationY = (d.coerceIn(-3f, 3f) * TILT_DEGREES)
                                .coerceIn(-maxTiltDegrees, maxTiltDegrees)
                            cameraDistance = 6f * cardScale * density.density
                        }
                        // bayangan lembut (satu bentuk di-blur, ikut berputar bersama kartu)
                        .drawBehind {
                            val r = cardRadius.toPx()
                            val dy = SHADOW_OFFSET_Y.toPx()
                            drawIntoCanvas { canvas ->
                                canvas.nativeCanvas.drawRoundRect(
                                    0f, dy, size.width, size.height + dy, r, r, shadowPaint
                                )
                            }
                        }
                        .clip(RoundedCornerShape(cardRadius))
                        .background(OndeMandeCardImageBg)
                        // efek pudar: lapisan krem tipis di atas foto (lebih ringan dari alpha)
                        .drawWithContent {
                            drawContent()
                            val fade = (offsetOf().absoluteValue * FADE_PER_STEP).coerceIn(0f, 0.35f)
                            if (fade > 0f) drawRect(OndeMandeCream.copy(alpha = fade))
                        }
                ) {
                    Image(
                        painter = painterResource(id = menuItem.imageRes),
                        contentDescription = menuItem.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
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