package id.djaka.splitbillapp.util

expect fun Double.toReadableCurrency(): String

fun toReadableCurrency(value: Double): String {
    return value.toReadableCurrency()
}