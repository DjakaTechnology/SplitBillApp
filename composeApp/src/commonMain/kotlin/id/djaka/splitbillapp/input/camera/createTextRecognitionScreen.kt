package id.djaka.splitbillapp.input.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class TextRecognitionState {
    var onStartScan: () -> Unit = {}
    var onFinishedScan: (String) -> Unit = {}
}

@Composable
fun rememberTextRecognitionState(onFinishScan: (String) -> Unit = {}): TextRecognitionState {
    return remember { TextRecognitionState().apply {
        onFinishedScan = onFinishScan
    } }
}

@Composable
expect fun createTextRecognitionScreen(state: TextRecognitionState = rememberTextRecognitionState())