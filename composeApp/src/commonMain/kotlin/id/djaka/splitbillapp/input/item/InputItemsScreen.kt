package id.djaka.splitbillapp.input.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.assign.InputAssignItemScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.toReadableCurrency

@OptIn(ExperimentalVoyagerApi::class)
data class InputItemsScreen(
    val id: String
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<InputItemScreenModel>()
        LifecycleEffectOnce {
            screenModel.id = id
            screenModel.onCreate()
        }
        CoreTheme {
            InputItemsWidget(
                onClickNext = {
                    navigator.push(
                        InputAssignItemScreen(id)
                    )
                },
                total = screenModel.total.collectAsState(0.0).value,
                onClickAddItem = {
                    screenModel.addMenuItem()
                },
                onClickAddFee = {
                    screenModel.addFeeItem()
                },
                itemList = screenModel.menuItems,
                feeList = screenModel.feeItem,
                onFeeItemChange = { index, name, price ->
                    screenModel.feeItem[index] =
                        screenModel.feeItem[index].copy(name = name, price = price)
                },
                onDeleteFee = { index ->
                    screenModel.feeItem.removeAt(index)
                },
                onMenuItemChange = { index, name, price, qty, total ->
                    screenModel.onHandleMenuItemChange(index, name, price, qty, total)
                },
                onDeleteMenuItem = { index ->
                    screenModel.menuItems.removeAt(index)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputItemsWidget(
    total: Double = 100_000.0,
    itemList: List<InputItemScreenModel.MenuItem>,
    feeList: List<InputItemScreenModel.FeeItem>,
    onClickNext: () -> Unit = {},
    onClickAddItem: () -> Unit = {},
    onClickAddFee: () -> Unit = {},
    onFeeItemChange: (index: Int, name: String, price: String) -> Unit = { _, _, _ -> },
    onMenuItemChange: (index: Int, name: String, price: String, qty: String, total: String) -> Unit = { _, _, _, _, _ -> },
    onDeleteFee: (index: Int) -> Unit = { _ -> },
    onDeleteMenuItem: (index: Int) -> Unit = { _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Input Items")
            })
        }
    ) {
        Box(Modifier.fillMaxSize().padding(it)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
                modifier = Modifier.padding(horizontal = Spacing.m)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
                    itemList.fastForEachIndexed { index, it ->
                        Item(
                            it.name,
                            it.qty,
                            it.total,
                            it.price,
                            priceHint = it.priceAutoFill.takeIf { it != 0.0 }?.toReadableCurrency()
                                ?: "",
                            totalHint = it.totalAutoFill.takeIf { it != 0.0 }?.toReadableCurrency()
                                ?: "",
                            onDelete = { onDeleteMenuItem(index) },
                            onNameChange = { name ->
                                onMenuItemChange(
                                    index,
                                    name,
                                    it.price,
                                    it.qty,
                                    it.total
                                )
                            },
                            onQtyChange = { qty ->
                                onMenuItemChange(
                                    index,
                                    it.name,
                                    it.price,
                                    qty,
                                    it.total
                                )
                            },
                            onTotalChange = { total ->
                                onMenuItemChange(
                                    index,
                                    it.name,
                                    it.price,
                                    it.qty,
                                    total
                                )
                            },
                            onPriceChange = { price ->
                                onMenuItemChange(
                                    index,
                                    it.name,
                                    price,
                                    it.qty,
                                    it.total
                                )
                            },
                        )
                    }
                    TextButton(onClick = onClickAddItem, modifier = Modifier.align(Alignment.End)) {
                        Icon(Icons.Filled.Add, "add")
                        Text("Add Item")
                    }
                }

                HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp)

                Column(Modifier.fillMaxWidth()) {
                    feeList.forEachIndexed { index, it ->
                        FeeItem(
                            Modifier.fillMaxWidth(),
                            it.name,
                            it.price,
                            onNameChange = { name -> onFeeItemChange(index, name, it.price) },
                            onTotalChange = { total ->
                                onFeeItemChange(
                                    index,
                                    it.name,
                                    total
                                )
                            },
                            onDelete = { onDeleteFee(index) }
                        )
                    }
                    TextButton(onClick = onClickAddFee, modifier = Modifier.align(Alignment.End)) {
                        Icon(Icons.Filled.Add, "add")
                        Text("Add Fee")
                    }
                }
            }

            InputItemBottomBar(
                Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                onClickNext = onClickNext,
                total = total
            )
        }
    }
}

@Composable
private fun InputItemBottomBar(
    modifier: Modifier = Modifier,
    onClickNext: () -> Unit,
    total: Double
) {
    Card(
        modifier,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(Spacing.m)
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

@Composable
private fun FeeItem(
    modifier: Modifier = Modifier,
    name: String,
    total: String,
    onNameChange: (String) -> Unit = {},
    onTotalChange: (String) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, "add")
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = name,
                label = { Text("Fee") },
                onValueChange = onNameChange,
                modifier = Modifier.weight(0.5f),
            )
            Spacer(Modifier.width(64.dp))
            OutlinedTextField(
                value = total,
                label = { Text("Total") },
                onValueChange = onTotalChange,
                modifier = Modifier.weight(0.5f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            visualTransformation = CurrencyVisualTransformation()
            )
        }
    }
}

@Composable
private fun Item(
    name: String,
    qty: String,
    total: String,
    price: String,
    priceHint: String = "",
    totalHint: String = "",
    onDelete: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onQtyChange: (String) -> Unit = {},
    onTotalChange: (String) -> Unit = {},
    onPriceChange: (String) -> Unit = {}
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, "add")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Column(Modifier.weight(0.5f)) {
                OutlinedTextField(
                    value = name,
                    label = { Text("Name") },
                    onValueChange = onNameChange,
                )
                OutlinedTextField(
                    value = price,
                    label = {
                        if (price.isEmpty() && priceHint.isNotEmpty()) Text(priceHint) else Text(
                            "Price"
                        )
                    },
                    onValueChange = onPriceChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                )
            }
            OutlinedTextField(
                value = qty,
                label = { Text("Qty") },
                onValueChange = {
                    onQtyChange(it)
                },
                modifier = Modifier.width(64.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = total,
                label = { if (total.isEmpty() && totalHint.isNotEmpty()) Text(totalHint) else Text("Total") },
                onValueChange = onTotalChange,
                modifier = Modifier.weight(0.5f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}