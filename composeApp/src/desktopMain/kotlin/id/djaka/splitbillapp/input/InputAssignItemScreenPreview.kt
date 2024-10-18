package id.djaka.splitbillapp.input

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import id.djaka.splitbillapp.input.assign.add_member.AddMemberSheet
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
            onClickMenuItem = {},
        )
    }
}

@Composable
@Preview
private fun AddMemberSheetPreview() {
    CoreTheme {
       Surface {
           Box(Modifier.fillMaxSize()) {
               AddMemberSheet(
                   onClickNew = {}
               )
           }
       }
    }
}