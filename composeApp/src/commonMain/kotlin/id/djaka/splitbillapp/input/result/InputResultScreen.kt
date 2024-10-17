package id.djaka.splitbillapp.input.result

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastJoinToString
import cafe.adriel.voyager.core.screen.Screen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.toReadableCurrency

class InputResultScreen : Screen {
    @Composable
    override fun Content() {
        CoreTheme {
            InputResultScreenWidget(
                member = listOf(
                    InputResultScreenModel.Member(
                        name = "Member 1",
                        total = 10000.0,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResultScreenWidget(
    member: List<InputResultScreenModel.Member>,
    invoice: InputResultScreenModel.InvoiceDetail
) {
    var isInvoiceModalVisible by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Phoenix Omurice")
        }, actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Edit, "edit")
            }
        })
    }) {
        Column(Modifier.padding(it), verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            Row(
                Modifier.fillMaxWidth().padding(start = Spacing.m, end = Spacing.xxs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Japan - 24 November", style = MaterialTheme.typography.labelSmall)
                    Text("10.000", style = MaterialTheme.typography.headlineSmall)
                }
                Row {
                    IconButton(onClick = { isInvoiceModalVisible = true }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ReceiptLong,
                            "next",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Share, "share")
                    }
                }
            }

            Column(
                Modifier.padding(horizontal = Spacing.m),
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
                    member.fastForEach {
                        MemberSectionItem(it)
                    }
                }

                if (isInvoiceModalVisible) {
                    ModalBottomSheet(onDismissRequest = { isInvoiceModalVisible = false }) {
                        Column(Modifier.padding(horizontal = Spacing.ml)) {
                            InvoiceDetailSection(invoice)
                        }
                    }
                }
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
                10000.0.toReadableCurrency(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun MemberSectionItem(it: InputResultScreenModel.Member) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.clip(CircleShape)
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(it.name, style = MaterialTheme.typography.labelMedium)
                    Text(
                        it.total.toReadableCurrency(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                var isChecked by remember { mutableStateOf(true) }
                Box(contentAlignment = Alignment.CenterEnd) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isChecked,
                        enter = slideInHorizontally { it * 4 },
                        exit = slideOutHorizontally { it * 2 }
                    ) {
                        Row(
                            Modifier.scale(1.8f).alpha(0.2f).offset(x = -18.dp),
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
                                fontSize = 36.sp,
                            )
                        }
                    }
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = !isChecked },
                        thumbContent = {
                            if (isChecked) {
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
            it.menuItem.fastForEach {
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
                item.total.toReadableCurrency(),
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
                item.price.toReadableCurrency(),
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