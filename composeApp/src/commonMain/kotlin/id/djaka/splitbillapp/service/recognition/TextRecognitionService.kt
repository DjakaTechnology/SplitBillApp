package id.djaka.splitbillapp.service.recognition

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.functions

class TextRecognitionService {
    suspend fun recognizeTextToModel(text: String): TextRecognitionReceiptModel {
        return Firebase.functions.httpsCallable("convertTextToReceipt").invoke(
            mapOf(
                "text" to text
            )
        )
            .data(TextRecognitionReceiptModel.serializer())
    }
}