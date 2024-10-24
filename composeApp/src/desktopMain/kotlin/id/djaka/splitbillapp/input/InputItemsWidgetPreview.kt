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
            itemList = listOf(
                InputItemScreenModel.MenuItem(
                    name = "Nasi Goreng",
                    price = "200000",
                    qty = "2",
                    total = "2000000",
                    id = "1"
                ),
                InputItemScreenModel.MenuItem(
                    name = "Nasi Goreng haha hih hihi haha",
                    price = "",
                    qty = "",
                    total = "",
                    id = "1"
                ),
            ),
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