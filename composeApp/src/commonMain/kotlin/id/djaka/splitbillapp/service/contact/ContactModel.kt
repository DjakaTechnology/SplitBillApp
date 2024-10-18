package id.djaka.splitbillapp.service.contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("serverID")
    val serverID: String?,
)