package id.djaka.splitbillapp.service.trip

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripModel(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("startDate")
    val startDate: Long? = 0,
    @SerialName("endDate")
    val endDate: Long? = 0,
    @SerialName("member")
    val member: List<Member> = emptyList(),
    @SerialName("ownerServerId")
    val ownerServerId: String? = null,
) {
    val isCurrentlyActive: Boolean
        get() {
            if (startDate == null || endDate == null) return false
            val currentTime = System.currentTimeMillis()
            return currentTime in startDate..endDate
        }

    @Serializable
    data class Member(
        @SerialName("id")
        val id: String = "",
        @SerialName("name")
        val name: String = "",
        @SerialName("serverId")
        val serverId: String?,
    )
}