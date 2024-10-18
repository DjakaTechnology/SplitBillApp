package id.djaka.splitbillapp.util

import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

val readableDateFormat = DateTimeComponents.Format {
    dayOfMonth(Padding.SPACE)
    char(' ')
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year(Padding.SPACE)
}