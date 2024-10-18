package id.djaka.splitbillapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.camera.InputCameraScreen
import id.djaka.splitbillapp.input.result.InputResultScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.widget.SplitBillItem

class HomeScreen : Screen {
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<HomeScreenModel>()
        LifecycleEffectOnce {
            model.onCreate()
        }
        val navigator = LocalNavigator.currentOrThrow
        CoreTheme {
            HomeWidget(
                onClickAdd = {
                    navigator.push(
                        InputCameraScreen()
                    )
                },
                data = model.bills.collectAsState(emptyList()).value,
                onClickBill = {
                    navigator.push(
                        InputResultScreen(it.id)
                    )
                }
            )
        }
    }
}

@Composable
fun HomeWidget(
    onClickAdd: () -> Unit = {},
    data: List<BillModel>,
    onClickBill: (BillModel) -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAdd) {
                Icon(Icons.Filled.Add, "add")
            }
        }
    ) {
        Column(
            Modifier.fillMaxWidth().padding(Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            TripSection()
            BillSection(data, onClickBill)
        }
    }
}

@Composable
private fun BillSection(data: List<BillModel>, onClickAdd: (index: BillModel) -> Unit = {}) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Text(
            "Recent Split Bills",
            style = MaterialTheme.typography.headlineMedium
        )

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            data.fastForEach {
                SplitBillItem(it, onClick = { onClickAdd(it) })
            }
        }
    }
}

@Composable
private fun TripSection() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Trips",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = {}) {
                Icon(Icons.Filled.Add, "add")
                Text("New")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
            TripCard()
            TripCard()
        }
    }
}

@Composable
private fun TripCard() {
    Card {
        Column(
            Modifier.padding(Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
            ) {
                Text("Japan Trip", style = MaterialTheme.typography.titleMedium)
                Badge {
                    Text("Now")
                }
            }
            Text("1 - 24 January", style = MaterialTheme.typography.labelSmall)
        }
    }
}