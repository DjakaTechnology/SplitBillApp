package id.djaka.splitbillapp.util

import java.text.DecimalFormat

actual fun Double.toReadableCurrency(): String {
    val format = DecimalFormat("#,###.##")
    return format.format(this)
}