package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.result.InputResultDetailWidget
import id.djaka.splitbillapp.input.result.InputResultScreenModel
import id.djaka.splitbillapp.input.result.InputResultScreenWidget
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
private fun InputResultScreenPreview() {
    CoreTheme {
        InputResultScreenWidget(
            member = listOf(
                InputResultScreenModel.Member(
                    name = "Member 1",
                    total = 10000.0,
                    id = "id",
                    menuItem = listOf(
                        InputResultScreenModel.Member.MenuItem(
                            name = "Item 1",
                            price = 10000.0
                        ),
                        InputResultScreenModel.Member.MenuItem(
                            name = "Item 2",
                            price = 10000.0
                        )
                    )
                )
            ),
            onPaidChange = {_ ,_ ->},
            invoice = InputResultScreenModel.InvoiceDetail(
                items = listOf(
                    InputResultScreenModel.InvoiceDetail.Item(
                        name = "Item 1",
                        price = 10000.0,
                        qty = 1,
                        total = 10000.0,
                        member = listOf("Member 1", "Member 2")
                    ),
                    InputResultScreenModel.InvoiceDetail.Item(
                        name = "Item 1",
                        price = 10000.0,
                        qty = 1,
                        total = 10000.0,
                        member = listOf("Member 1", "Member 2", "Member 3", "Member 4")
                    ),
                ),
                fees = listOf(
                    InputResultScreenModel.InvoiceDetail.Fee(
                        name = "Discount",
                        price = -10000.0
                    ),
                    InputResultScreenModel.InvoiceDetail.Fee(
                        name = "Tax",
                        price = 10000.0
                    )
                )
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun InputResultDetailWidgetPreview() {
    CoreTheme {
        Scaffold {
            InputResultDetailWidget()
        }
    }
}