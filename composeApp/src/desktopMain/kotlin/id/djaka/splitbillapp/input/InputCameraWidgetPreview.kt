package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.camera.InputCameraWidget
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
private fun InputCameraWidgetPreview() {
    CoreTheme {
        InputCameraWidget(snackbarHostState)
    }
}