package id.djaka.splitbillapp.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.input.camera.InputCameraScreen
import id.djaka.splitbillapp.input.result.InputResultScreen
import id.djaka.splitbillapp.login.LoginScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.profile.ProfileScreen
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.trip.TripModel
import id.djaka.splitbillapp.trip.TripScreen
import id.djaka.splitbillapp.util.readableDateFormat
import id.djaka.splitbillapp.widget.SplitBillItem
import kotlinx.datetime.Instant
import kotlinx.datetime.format

class HomeScreen : Screen {
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<HomeScreenModel>()
        LifecycleEffectOnce {
            model.onCreate()
        }
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(navigator) {
            if (Firebase.auth.currentUser == null) navigator.push(LoginScreen())
        }
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
                },
                tripData = model.tripList.collectAsState(emptyList()).value,
                onClickTrip = {
                    navigator.push(
                        TripScreen(it.id)
                    )
                },
                onAddTrip = {
                    model.addTrip(it)
                },
                tripMap = model.trip.collectAsState(emptyMap()).value,
                onClickProfile = {
                    navigator.push(
                        ProfileScreen()
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWidget(
    onClickAdd: () -> Unit = {},
    data: List<BillModel>,
    onClickBill: (BillModel) -> Unit = {},
    tripData: List<TripModel> = listOf(),
    onClickTrip: (TripModel) -> Unit = {},
    onAddTrip: (state: HomeAddTripSheetState) -> Unit = {},
    tripMap: Map<String, TripModel> = emptyMap(),
    onClickProfile: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAdd) {
                Icon(Icons.Filled.Add, "add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = onClickProfile) {
                        Icon(Icons.Filled.AccountCircle, "profile")
                    }
                }
            )
        }
    ) {
        var isShowAddTripSheet by remember { mutableStateOf(false) }
        if (isShowAddTripSheet) {
            val state = rememberHomeAddTripSheetState()
            ModalBottomSheet(
                onDismissRequest = { isShowAddTripSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                HomeAddTripSheet(
                    onClickSave = {
                        isShowAddTripSheet = false
                        onAddTrip(state)
                    },
                    state = state,
                    modifier = Modifier.fillMaxWidth().padding(Spacing.ml)
                )
            }
        }

        Column(
            Modifier.fillMaxWidth().padding(it).padding(Spacing.m)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            TripSection(tripData, onClickTrip, {
                isShowAddTripSheet = true
            })
            BillSection(data, onClickBill, onClickAddBill = { onClickAdd() }, tripMap = tripMap)
        }
    }
}

@Composable
private fun BillSection(
    data: List<BillModel>,
    onClickBill: (index: BillModel) -> Unit = {},
    onClickAddBill: () -> Unit,
    tripMap: Map<String, TripModel>
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Recent Split Bills",
                style = MaterialTheme.typography.headlineMedium
            )
            TextButton(onClick = onClickAddBill) {
                Icon(Icons.Filled.Add, "add")
                Text("New Bills")
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
            data.fastForEach {
                SplitBillItem(it, onClick = { onClickBill(it) }, tripModel = tripMap[it.tripId])
            }
        }
    }
}

@Composable
private fun TripSection(
    tripData: List<TripModel>,
    onClickTrip: (TripModel) -> Unit = {},
    onClickAddTrip: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Trips",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = onClickAddTrip) {
                Icon(Icons.Filled.Add, "add")
                Text("New Trip")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            modifier = Modifier.horizontalScroll(
                rememberScrollState()
            )
        ) {
            tripData.fastForEach {
                TripCard(it, onClick = { onClickTrip(it) })
            }
        }
    }
}

@Composable
private fun TripCard(tripModel: TripModel, onClick: () -> Unit) {
    Card(Modifier.clickable { onClick() }) {
        Column(
            Modifier.padding(Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)
            ) {
                Text(tripModel.name, style = MaterialTheme.typography.titleMedium)
                if (tripModel.isCurrentlyActive) {
                    Badge {
                        Text("Now")
                    }
                }
            }
            if (tripModel.startDate !== null && tripModel.endDate !== null) {
                Text(
                    "${
                        Instant.fromEpochMilliseconds(tripModel.startDate)
                            .format(readableDateFormat)
                    } - ${
                        Instant.fromEpochMilliseconds(tripModel.endDate).format(readableDateFormat)
                    }",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}