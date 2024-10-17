package id.djaka.splitbillapp.platform

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import splitbillapp.composeapp.generated.resources.Res
import splitbillapp.composeapp.generated.resources.inter_black
import splitbillapp.composeapp.generated.resources.inter_bold
import splitbillapp.composeapp.generated.resources.inter_light
import splitbillapp.composeapp.generated.resources.inter_medium
import splitbillapp.composeapp.generated.resources.inter_regular

val CoreTypography
    @Composable get() = Typography().copy(
        displayLarge = MaterialTheme.typography.displayLarge.copy(
            fontFamily = CoreFontFamily,
        ),
        displayMedium = MaterialTheme.typography.displayMedium.copy(
            fontFamily = CoreFontFamily,
        ),
        displaySmall = MaterialTheme.typography.displaySmall.copy(
            fontFamily = CoreFontFamily,
        ),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = CoreFontFamily,
        ),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(
            fontFamily = CoreFontFamily,
        ),
        titleMedium = MaterialTheme.typography.titleMedium.copy(
            fontFamily = CoreFontFamily,
        ),
        titleSmall = MaterialTheme.typography.titleSmall.copy(
            fontFamily = CoreFontFamily,
        ),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = CoreFontFamily,
        ),
        bodySmall = MaterialTheme.typography.bodySmall.copy(
            fontFamily = CoreFontFamily,
        ),
        labelLarge = MaterialTheme.typography.labelLarge.copy(
            fontFamily = CoreFontFamily,
        ),
        labelMedium = MaterialTheme.typography.labelMedium.copy(
            fontFamily = CoreFontFamily,
        ),
        labelSmall = MaterialTheme.typography.labelSmall.copy(
            fontFamily = CoreFontFamily,
        ),
    )

val CoreFontFamily
    @Composable get() = FontFamily(
        Font(
            Res.font.inter_bold,
            FontWeight.Bold
        ),
        Font(
            Res.font.inter_regular,
            FontWeight.Normal
        ),
        Font(
            Res.font.inter_black,
            FontWeight.Black
        ),
        Font(
            Res.font.inter_medium,
            FontWeight.Medium
        ),
        Font(
            Res.font.inter_light,
            FontWeight.Light,
        )
    )