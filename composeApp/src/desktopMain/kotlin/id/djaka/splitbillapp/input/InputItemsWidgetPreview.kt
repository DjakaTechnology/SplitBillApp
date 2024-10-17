package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.item.InputItemScreenModel
import id.djaka.splitbillapp.input.item.InputItemsWidget
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
private fun InputItemsWidgetPreview() {
    CoreTheme(isDarkMode = true) {
        InputItemsWidget(
            itemList = listOf(),
            feeList = listOf(
                InputItemScreenModel.FeeItem(
                    name = "Service Charge",
                    price = "10000",
                    id = "1"
                ),
            ),
        )
    }
}