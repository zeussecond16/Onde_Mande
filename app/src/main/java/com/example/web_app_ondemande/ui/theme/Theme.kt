package com.example.web_app_ondemande.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OndeMandeInk,
    onPrimary = OndeMandeSurface,
    primaryContainer = OndeMandeStepperBg,
    onPrimaryContainer = OndeMandeInk,
    secondary = OndeMandeMuted,
    onSecondary = OndeMandeSurface,
    background = OndeMandeCream,
    onBackground = OndeMandeInk,
    surface = OndeMandeSurface,
    onSurface = OndeMandeInk,
    surfaceVariant = OndeMandeStepperBg,
    onSurfaceVariant = OndeMandeMuted
)

/**
 * Tema utama Onde Mande dengan warna Krem & Hitam Charcoal khas Minang.
 * dynamicColor dimatikan agar warna konsisten di semua versi Android.
 */
@Composable
fun OndeMandeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
