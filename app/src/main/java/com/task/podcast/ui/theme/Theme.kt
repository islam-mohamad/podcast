package com.task.podcast.ui.theme

import abudhabi.tamm.uiwidget.compose.base.theme.Border
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.task.podcast.core.presentation.ColorPalette
import com.task.podcast.core.presentation.dimens.Elevation
import com.task.podcast.core.presentation.dimens.Radius
import com.task.podcast.core.presentation.dimens.Spaces

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

object PodcastTheme {

    val colors
        @ReadOnlyComposable
        @Composable
        get() = LocalColors.current

    val spaces
        @ReadOnlyComposable
        @Composable
        get() = LocalSpaces.current

    val radius
        @ReadOnlyComposable
        @Composable
        get() = LocalRadius.current

    val elevation
        @ReadOnlyComposable
        @Composable
        get() = LocalElevation.current

    val border
        @ReadOnlyComposable
        @Composable
        get() = LocalBorder.current
}

@Composable
fun PodcastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalSpaces provides Spaces(),
        LocalRadius provides Radius(),
        LocalElevation provides Elevation(),
        LocalBorder provides Border()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

private val LocalColors = staticCompositionLocalOf { ColorPalette }
private val LocalSpaces = staticCompositionLocalOf { Spaces() }
private val LocalRadius = staticCompositionLocalOf { Radius() }
private val LocalElevation = staticCompositionLocalOf { Elevation() }
private val LocalBorder = staticCompositionLocalOf { Border() }