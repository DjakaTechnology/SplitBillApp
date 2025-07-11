package id.djaka.splitbillapp.input.result

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastJoinToString
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.item.InputItemsScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.readableDateYearFormat
import id.djaka.splitbillapp.util.toReadableCurrency
import id.djaka.splitbillapp.widget.LoadingDialog
import id.djaka.splitbillapp.widget.PeopleWidget
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.format

data class InputResultScreen(
    val id: String
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<InputResultScreenModel>()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(screenModel) {
            screenModel.onCreate(id)
        }
        if (screenModel.id == "") {
            return
        }
        CoreTheme {
            val bills = screenModel.billData.collectAsState(null).value
            if (bills == null) {
                Scaffold {
                    LoadingDialog()
                }
                return@CoreTheme
            }

            InputResultScreenWidget(
                member = screenModel.members,
                invoice = screenModel.invoiceDetail,
                onPaidChange = { index, it ->
                    screenModel.setPaid(index, it)
                },
                onChangeDetail = {
                    screenModel.setDetailData(it)
                },
                onClose = {
                    navigator.pop()
                },
                name = bills.name,
                date = bills.date,
                tripList = screenModel.tripListName.collectAsState(listOf()).value,
                total = screenModel.total.collectAsState(0.0).value,
                onClickEdit = {
                    navigator.push(InputItemsScreen(id))
                },
                tripName = screenModel.tripName.collectAsState(null).value,
                paidById = bills.paidById
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResultScreenWidget(
    name: String = "Phoenix",
    date: Long = Clock.System.now().toEpochMilliseconds(),
    member: List<InputResultScreenModel.Member>,
    invoice: InputResultScreenModel.InvoiceDetail,
    tripList: List<String>,
    onPaidChange: (index: Int, isChecked: Boolean) -> Unit,
    onChangeDetail: (state: InputResultDetailWidgetState) -> Unit = {},
    onClose: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    total: Double = 0.0,
    tripName: String? = null,
    paidById: String? = null,
) {
    var isInvoiceModalVisible by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(name)
        }, actions = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, "close")
            }
        })
    }) {
        if (isInvoiceModalVisible) {
            ModalBottomSheet(
                onDismissRequest = { isInvoiceModalVisible = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(Modifier.padding(horizontal = Spacing.ml).padding(bottom = Spacing.m)) {
                    InvoiceDetailSection(invoice)
                }
            }
        }

        var isShowEditDetail by remember { mutableStateOf(false) }
        if (isShowEditDetail || name.isEmpty()) {
            val state = rememberInputResultDetailWidgetState(
                name = name,
                date = date,
                tripList = tripList,
                selectTripIndex = tripList.indexOf(tripName),
                members = member,
                selectedMemberIndex = member.indexOfFirst { it.id == paidById }.takeIf { it != -1 }
                    ?: 0
            )

            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            LaunchedEffect(sheetState.isVisible) {
                if (!sheetState.isVisible && name.isEmpty()) sheetState.show()
            }
            ModalBottomSheet(
                onDismissRequest = { isShowEditDetail = false },
                sheetState = sheetState
            ) {
                InputResultDetailWidget(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.ml),
                    state = state,
                    onClickSave = {
                        onChangeDetail(state)
                        isShowEditDetail = false
                    }
                )
            }
        }

        Column(Modifier.verticalScroll(rememberScrollState()).padding(it), verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            Header(
                onShowInvoice = { isInvoiceModalVisible = true },
                onClickEditDetail = {
                    isShowEditDetail = true
                },
                date = date,
                total = total,
                onClickEdit = onClickEdit,
                trip = tripName,
                paidByName = member.fastFirstOrNull { it.id == paidById }?.name
            )
            MemberSection(member, onPaidChange)
        }
    }
}

@Composable
private fun MemberSection(
    member: List<InputResultScreenModel.Member>,
    onPaidChange: (index: Int, isChecked: Boolean) -> Unit
) {
    Column(
        Modifier.padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            member.fastForEachIndexed { index, it ->
                MemberSectionItem(it, onCheckChange = {
                    onPaidChange(index, it)
                })
            }
        }
    }
}

@Composable
private fun Header(
    total: Double,
    onShowInvoice: () -> Unit,
    onClickEditDetail: () -> Unit = {},
    date: Long,
    trip: String?,
    onClickEdit: () -> Unit = {},
    paidByName: String? = null
) {
    Row(
        Modifier.fillMaxWidth().padding(start = Spacing.m, end = Spacing.xxs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Column {
                Text(
                    listOf(
                        trip ?: "No Trip",
                        Instant.fromEpochMilliseconds(date).format(readableDateYearFormat),
                    ).fastJoinToString(" - "),
                    style = MaterialTheme.typography.labelSmall,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onClickEditDetail()
                    }
                )
                if (paidByName != null) {
                    Text(
                        "Paid by $paidByName",
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
            Text(total.toReadableCurrency(), style = MaterialTheme.typography.headlineSmall)
        }
        Row {
            IconButton(onClick = { onShowInvoice() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ReceiptLong,
                    "recipt",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onClickEdit) {
                Icon(Icons.Filled.Edit, "edit", modifier = Modifier.size(24.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Share, "share", modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
private fun InvoiceDetailSection(invoice: InputResultScreenModel.InvoiceDetail) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text("Invoice Detail", style = MaterialTheme.typography.headlineSmall)
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
            invoice.items.fastForEach {
                InvoiceDetailItem(it)
            }
            invoice.fees.fastForEach {
                InvoiceDetailFee(it)
            }
        }
        HorizontalDivider(thickness = 1.dp)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Total",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                remember(invoice) {
                    invoice.items.sumOf { it.total } + invoice.fees.sumOf { it.price }
                }.toReadableCurrency(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun MemberSectionItem(
    data: InputResultScreenModel.Member,
    onCheckChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PeopleWidget(text = data.name, isShowLabel = false)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(data.name, style = MaterialTheme.typography.labelMedium)
                    Text(
                        data.total.toReadableCurrency(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(contentAlignment = Alignment.CenterEnd) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = data.isPaid,
                        enter = slideInHorizontally { it * 4 },
                        exit = slideOutHorizontally { it * 2 }
                    ) {
                        Row(
                            Modifier.width(128.dp).scale(2.4f).alpha(0.2f).offset(x = -18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // I dont know why but without this spacing, it will clip
                            Spacer(Modifier.width(Spacing.s))
                            Icon(
                                Icons.Filled.Check,
                                "checkmark",
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                "PAID",
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Visible
                            )
                        }
                    }
                    Switch(
                        checked = data.isPaid,
                        onCheckedChange = { onCheckChange(!data.isPaid) },
                        thumbContent = {
                            if (data.isPaid) {
                                Icon(
                                    Icons.Filled.Check,
                                    "checked",
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.AttachMoney,
                                    "unchecked",
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        })
                }
            }
        }

        HorizontalDivider(thickness = 1.dp)

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
            modifier = Modifier.fillMaxWidth()
        ) {
            data.menuItem.fastForEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it.name, style = MaterialTheme.typography.labelMedium)

                    Text(
                        it.price.toReadableCurrency(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun InvoiceDetailItem(item: InputResultScreenModel.InvoiceDetail.Item) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                item.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                item.price.toReadableCurrency(),
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.7f)
            )
        }
        Text(
            "x${item.qty}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = Spacing.m)
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Text(
                item.total.toReadableCurrency(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                "(${item.member.size}) ${item.member.fastJoinToString()}",
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun InvoiceDetailFee(item: InputResultScreenModel.InvoiceDetail.Fee) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                item.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Text(
                item.price.toReadableCurrency(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}