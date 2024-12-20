package id.djaka.splitbillapp.service.bill

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillModel(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("date")
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    @SerialName("items")
    val items: List<Item> = listOf(),
    @SerialName("feeItems")
    val feeItems: List<FeeItem> = listOf(),
    @SerialName("members")
    val members: List<Member> = listOf(),
    @SerialName("ownerServerId")
    val ownerServerId: String? = null,
    @SerialName("tripId")
    val tripId: String? = null,
    @SerialName("paidById")
    val paidById: String? = null
) {
    @Serializable
    data class Member(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("isPaid")
        val isPaid: Boolean,
        @SerialName("serverId")
        val serverId: String? = null
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
        val pricePerMember = total / memberIds.size
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