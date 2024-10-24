package id.djaka.splitbillapp.util

fun Double.toReadableCurrency(): String {
    val decimalFormatter = DecimalFormatter()
    return decimalFormatter.formatForVisual(this.toString())
}