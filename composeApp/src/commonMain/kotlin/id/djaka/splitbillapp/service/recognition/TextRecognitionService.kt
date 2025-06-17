package id.djaka.splitbillapp.service.recognition

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.functions
import kotlin.time.Duration.Companion.seconds

class TextRecognitionService {
    suspend fun recognizeTextToModel(text: String): TextRecognitionReceiptModel {
        return Firebase.functions.httpsCallable("convertTextToReceipt", timeout = 30.seconds).invoke(
            mapOf(
                "text" to text
            ),
        )
            .data(TextRecognitionReceiptModel.serializer())
    }
}