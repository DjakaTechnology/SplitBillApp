package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.assign.InputAssignItemWidget
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
private fun InputAssignItemScreenPreview() {
    CoreTheme(isDarkMode = true) {
        InputAssignItemWidget()
    }
}