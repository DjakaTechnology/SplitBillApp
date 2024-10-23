package id.djaka.splitbillapp.service.recognition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TextRecognitionReceiptModel(
    @SerialName("items")
    val items: List<Item>,
    @SerialName("fee")
    val fee: List<Fee>,
    @SerialName("total")
    val total: Double,
) {
    @Serializable
    class Item(
        @SerialName("name")
        val name: String,
        @SerialName("price")
        val price: Double?,
        @SerialName("qty")
        val qty: Int,
        @SerialName("total")
        val total: Double?
    )

    @Serializable
    class Fee(
        @SerialName("name")
        val name: String,
        @SerialName("price")
        val price: Double?,
        @SerialName("discountn")
        val discount: Double?,
    )
}