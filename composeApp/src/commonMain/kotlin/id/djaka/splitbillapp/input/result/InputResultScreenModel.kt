package id.djaka.splitbillapp.input.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class InputResultScreenModel : ScreenModel {

    val members = mutableStateListOf<Member>()

    var invoiceDetail by mutableStateOf(InvoiceDetail(emptyList(), emptyList()))

    data class Member(
        val name: String,
        val total: Double,
        val menuItem: List<MenuItem>
    ) {
        data class MenuItem(
            val name: String,
            val price: Double
        )
    }

    data class InvoiceDetail(
        val items: List<Item>,
        val fees: List<Fee>
    ) {
        data class Item(
            val name: String,
            val price: Double,
            val qty: Int,
            val total: Double,
            val member: List<String>
        )

        data class Fee(
            val name: String,
            val price: Double
        )
    }
}