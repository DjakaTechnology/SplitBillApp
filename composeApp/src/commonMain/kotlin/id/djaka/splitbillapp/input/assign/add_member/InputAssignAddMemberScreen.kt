package id.djaka.splitbillapp.input.assign.add_member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.service.contact.ContactModel

class AddMemberSheetState {
    var member by mutableStateOf("")
    var items by mutableStateOf(listOf<ContactModel>())
    var existingMember by mutableStateOf(setOf<String>())
}

@Composable
fun rememberAddMemberSheetState(
    items: List<ContactModel> = listOf(),
    existingMember: Set<String> = setOf()
) = remember(items, existingMember) {
    AddMemberSheetState().apply {
        this.items = items
        this.existingMember = existingMember
    }
}

@Composable
fun AddMemberSheet(
    modifier: Modifier = Modifier,
    state: AddMemberSheetState = rememberAddMemberSheetState(),
    onClickNew: () -> Unit = {},
    onClickContact: (ContactModel) -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.m), modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            OutlinedTextField(
                value = state.member,
                onValueChange = { state.member = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Write Name") }
            )
        }

        HorizontalDivider(thickness = 1.dp)

        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {

            if (state.member.isNotEmpty()) {
                item {
                    Item(
                        name = "Create: ${state.member}",
                        onClick = onClickNew,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(state.items.filter { state.member.isEmpty() || it.name.contains(state.member) }) {
                Item(
                    name = it.name,
                    onClick = { onClickContact(it) },
                    modifier = Modifier.fillMaxWidth(),
                    isBillMember = state.existingMember.contains(it.id)
                )
            }
        }
    }
}

@Composable
private fun Item(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isBillMember: Boolean = false
) {
    Card(
        modifier = modifier.alpha(
            if (isBillMember) 0.5f else 1f
        ), onClick = onClick
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = Spacing.m, vertical = Spacing.s),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name)
            if (isBillMember) {
                Icon(Icons.Filled.CheckCircle, "check")
            }
        }
    }
}