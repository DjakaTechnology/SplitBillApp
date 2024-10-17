package id.djaka.splitbillapp.input.assign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.result.InputResultScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.toReadableCurrency

class InputAssignItemScreen : Screen {
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        CoreTheme {
            val navigator = LocalNavigator.currentOrThrow
            val screenModel = getScreenModel<InputAssignItemScreenModel>()
            LifecycleEffectOnce {
                screenModel.onCreate()
            }
            val currentMember = screenModel.currentSelectedMember
                ?: screenModel.memberItem.firstOrNull()?.id
            InputAssignItemWidget(
                screenModel.menuItem,
                currentMember,
                screenModel.memberItem,
                onClickMenuItem = {
                    if (currentMember == null) return@InputAssignItemWidget
                    screenModel.toggleAssignMember(it, currentMember)
                },
                total = screenModel.total.collectAsState(0.0).value,
                onClickMemberItem = {
                    screenModel.selectMember(it)
                },
                onClickCloseMemberItem = {
                    screenModel.removeMember(it)
                },
                onAddMember = {
                    screenModel.addNewMember(it)
                },
                onClickNext = {
                    navigator.push(InputResultScreen())
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputAssignItemWidget(
    menuItems: List<InputAssignItemScreenModel.MenuItem>,
    selectedMember: String?,
    memberList: List<InputAssignItemScreenModel.MemberItem>,
    onClickMenuItem: (index: Int) -> Unit,
    feeList: List<InputAssignItemScreenModel.FeeItem> = listOf(),
    total: Double = 0.0,
    onClickMemberItem: (index: Int) -> Unit = {},
    onClickCloseMemberItem: (index: Int) -> Unit = {},
    onAddMember: (name: String) -> Unit = {},
    onClickNext: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Assign Member")
            }, navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
                }
            })
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            if (false) {
                ModalBottomSheet(onDismissRequest = {}) {
                    var member by remember { mutableStateOf("") }
                    AddMemberSheet(
                        member = member,
                        onMemberChange = {
                            member = it
                        },
                        onClickSave = {
                            onAddMember(member)
                            member = ""
                        }
                    )
                }
            }

            Column(
                Modifier.padding(it).padding(horizontal = Spacing.m),
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                MemberSection(
                    selectedMember,
                    memberList,
                    onClickItem = {
                        onClickMemberItem(it)
                    },
                    onClickClose = {
                        onClickCloseMemberItem(it)
                    }
                )

                SplitSection(menuItems, onClickMenuItem, selectedMember)

                HorizontalDivider(Modifier.fillMaxWidth(), 1.dp)
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.s),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    feeList.fastForEachIndexed { index, it ->
                        FeeItem(
                            it.name,
                            it.price
                        )
                    }
                }
            }

            AssignItemBottomBar(
                Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                total,
                onClickNext
            )
        }
    }
}

@Composable
private fun FeeItem(
    label: String,
    price: Double
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label)
        Text(price.toReadableCurrency())
    }
}

@Composable
private fun MemberSection(
    selectedMember: String?,
    memberList: List<InputAssignItemScreenModel.MemberItem>,
    onClickClose: (index: Int) -> Unit = {},
    onClickItem: (index: Int) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
            memberList.fastForEachIndexed { index, item ->
                val isSelected = item.id == selectedMember || selectedMember == null && index == 0
                PeopleItem(
                    isSelected,
                    onClickClose = {
                        onClickClose(index)
                    },
                    onClick = {
                        onClickItem(index)
                    },
                    isShowClose = item.id != "YOU"
                )
            }
        }

        PeopleItem()
    }
}

@Composable
private fun SplitSection(
    menuItems: List<InputAssignItemScreenModel.MenuItem> = listOf(),
    onClickMenuItem: (index: Int) -> Unit = {},
    selectedMember: String?
) {
    Column {
        TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
            Text("Split Equally")
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            menuItems.fastForEachIndexed { index, it ->
                MenuItem(
                    it,
                    onClick = { onClickMenuItem(index) },
                    isChecked = it.memberIds.contains(selectedMember)
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    item: InputAssignItemScreenModel.MenuItem,
    onClick: () -> Unit = {},
    isChecked: Boolean = false
) {
    Card(Modifier.clickable {
        onClick()
    }) {
        Column(
            Modifier.fillMaxWidth().padding(Spacing.m),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(item.name)
                    Text(item.price.toReadableCurrency())
                }

                Text("x${item.qty}")

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    if (isChecked) {
                        Icon(Icons.Filled.RadioButtonChecked, "checked")
                    } else {
                        Icon(Icons.Filled.RadioButtonUnchecked, "checked")
                    }
                    Text(item.total.toReadableCurrency())
                }
            }

            val people = item.memberIds.size
            AnimatedVisibility(people > 0) {
                Column {
                    Spacer(Modifier.height(Spacing.xs))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                            repeat(minOf(people, 10)) {
                                Box(
                                    Modifier.size(16.dp)
                                        .clip(CircleShape)
                                        .background(SolidColor(Color.Gray))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurface,
                                            CircleShape
                                        )
                                )
                            }
                        }

                        Text(
                            "= ${item.pricePerMember.toReadableCurrency()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignItemBottomBar(
    modifier: Modifier = Modifier,
    total: Double,
    onClickNext: () -> Unit = {}
) {

    Card(
        modifier,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )
    ) {
        Column {
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp
                )
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Filled.DoubleArrow, "up",
                        modifier = Modifier.rotate(-90f).scale(0.7f)
                    )
                    Text("Preview Bill")
                }
            }

            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = Spacing.m, vertical = Spacing.s)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                ) {
                    Text("Total", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "Rp. ${total.toReadableCurrency()}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Button(onClick = onClickNext) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
private fun PeopleItem(
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onClickClose: () -> Unit = {},
    isShowClose: Boolean = true
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                Modifier.size(64.dp)
                    .clip(CircleShape)
                    .background(color = Color.Green)
                    .border(if (isSelected) 2.dp else 0.dp, Color.Blue, CircleShape)
                    .clickable {
                        onClick()
                    }
            ) {
                Text("A")
            }

            if (isShowClose) {
                Icon(
                    Icons.Filled.Close,
                    "delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        ).align(Alignment.TopEnd).clickable {
                            onClickClose()
                        }
                )
            }
        }
        Text("You")
    }
}

@Composable
fun AddMemberSheet(
    member: String,
    onMemberChange: (String) -> Unit = {},
    onClickSave: () -> Unit = {},
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = member,
            onValueChange = onMemberChange,
        )

        Button(onClick = onClickSave) {
            Text("Save")
        }
    }
}