package id.djaka.splitbillapp.platform

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

@Composable
fun CoreTheme(isDarkMode: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isDarkMode) LocalDarkColorScheme.current else LocalLightColorScheme.current,
        typography = CoreTypography,
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(16.dp)
        ),
    ) {
        content()
    }
}

val LocalDarkColorScheme = compositionLocalOf {
    darkColorScheme()
}

val LocalLightColorScheme = compositionLocalOf {
    lightColorScheme()
}