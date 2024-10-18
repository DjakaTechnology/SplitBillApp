package id.djaka.splitbillapp.util

import androidx.compose.ui.graphics.Color

fun createRandomColorFromName(name: String): Color {
    val hash = name.hashCode()
    val r = (hash and 0xFF0000 shr 16) / 255f
    val g = (hash and 0x00FF00 shr 8) / 255f
    val b = (hash and 0x0000FF) / 255f
    return Color(r, g, b)
}