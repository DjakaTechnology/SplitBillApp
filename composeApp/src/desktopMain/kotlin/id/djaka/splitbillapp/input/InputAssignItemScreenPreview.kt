package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.input.assign.InputAssignItemScreenModel
import id.djaka.splitbillapp.input.assign.InputAssignItemWidget
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
private fun InputAssignItemScreenPreview() {
    CoreTheme(isDarkMode = true) {
        InputAssignItemWidget(
            menuItems = listOf(
                InputAssignItemScreenModel.MenuItem(
                    id = "id",
                    name = "Name",
                    qty = 1,
                    price = 10000.0,
                    memberIds = setOf()
                ),
            ),
            selectedMember = null,
            memberList = listOf(
                InputAssignItemScreenModel.MemberItem(
                    id = "id",
                    name = "Name"
                ),
            ),
            onClickMenuItem = {}
        )
    }
}