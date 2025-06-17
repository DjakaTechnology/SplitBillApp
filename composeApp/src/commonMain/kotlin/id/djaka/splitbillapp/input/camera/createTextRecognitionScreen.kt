package id.djaka.splitbillapp.input.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class TextRecognitionState {
    var onStartScan: () -> Unit = {}
    var onFinishedScan: (String) -> Unit = {}
    var onFailedScan: (e: Exception) -> Unit = {}
}

@Composable
fun rememberTextRecognitionState(onFinishScan: (String) -> Unit = {}, onFailedScan: (e: Exception) -> Unit = {}): TextRecognitionState {
    return remember { TextRecognitionState().apply {
        onFinishedScan = onFinishScan
        this.onFailedScan = onFailedScan
    } }
}

@Composable
expect fun createTextRecognitionScreen(state: TextRecognitionState = rememberTextRecognitionState())