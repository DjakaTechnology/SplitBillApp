package id.djaka.splitbillapp.service.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

class TextRecognitionAnalyzer(
    private val onTextDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val textRecognizer = FirebaseVision.getInstance()
        .cloudTextRecognizer

    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    var iamge: FirebaseVisionImage? = null

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val imageRotation = degreesToFirebaseRotation(0)
        this.iamge = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
        imageProxy.close()
//        textRecognizer.processImage(image)
//            .addOnSuccessListener { firebaseVisionText ->
//                onTextDetected(firebaseVisionText.text)
//                imageProxy.close()
//            }
//            .addOnFailureListener { e ->
//            }
    }

    @OptIn(ExperimentalGetImage::class)
    fun startAnalyze(onFinished: (String) -> Unit) {
        textRecognizer.processImage(iamge ?: return)
            .addOnSuccessListener { firebaseVisionText ->
                onTextDetected(firebaseVisionText.text)
                onFinished(firebaseVisionText.text)
            }
            .addOnFailureListener { e ->
            }
    }

}