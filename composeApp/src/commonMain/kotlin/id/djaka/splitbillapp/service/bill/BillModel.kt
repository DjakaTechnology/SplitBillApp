package id.djaka.splitbillapp.service.bill

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillModel(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("date")
    val date: Long = 0,
    @SerialName("items")
    val items: List<Item> = listOf(),
    @SerialName("feeItems")
    val feeItems: List<FeeItem> = listOf(),
    @SerialName("members")
    val members: List<Member> = listOf()
) {
    @Serializable
    data class Member(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("isPaid")
        val isPaid: Boolean,
    )

    @Serializable
    data class Item(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("qty")
        val qty: Int,
        @SerialName("price")
        val price: Double,
        @SerialName("memberIds")
        val memberIds: Set<String>
    ) {
        val total = price * qty
    }

    @Serializable
    data class FeeItem(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("price")
        val price: Double
    )
}