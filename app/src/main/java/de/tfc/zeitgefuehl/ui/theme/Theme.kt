package de.tfc.zeitgefuehl.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = DarkBackground,
    primaryContainer = AccentPurple,
    onPrimaryContainer = AccentCyan,
    secondary = AccentEmerald,
    onSecondary = DarkBackground,
    tertiary = AccentAmber,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onBackground = Color(0xFFE4E7EB),
    onSurface = Color(0xFFE4E7EB),
    onSurfaceVariant = Color(0xFF9EA3B0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006874),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF9EEFFD),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF006A60),
    onSecondary = Color.White,
    tertiary = AccentPurple,
    background = Color(0xFFFBFDFD),
    surface = Color.White,
    surfaceVariant = Color(0xFFDAE4E6),
    onBackground = Color(0xFF191C1D),
    onSurface = Color(0xFF191C1D),
    onSurfaceVariant = Color(0xFF3F484A)
)

@Composable
fun ZeitgefuehlTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
