package id.djaka.splitbillapp.input.camera

import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.TextRecognitionScreen

@Composable
actual fun createTextRecognitionScreen(state: TextRecognitionState) {
    TextRecognitionScreen(state)
}