package id.djaka.splitbillapp.trip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.result.InputResultScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.trip.TripModel
import id.djaka.splitbillapp.util.toReadableCurrency
import id.djaka.splitbillapp.widget.PeopleWidget
import id.djaka.splitbillapp.widget.SplitBillItem

class TripScreen(val id: String) : Screen {
    @Composable
    override fun Content() {
        val model = getScreenModel<TripScreenModel>()
        val navigator = LocalNavigator.currentOrThrow
        var showDeleteDialog by remember { mutableStateOf(false) }

        LaunchedEffect(id) {
            model.tripId = id
            model.selectedMember = null
        }
        CoreTheme {
            val trip = model.trip.collectAsState(null).value
            TripScreenWidget(
                tripData = model.tripSummary.collectAsState(emptyList()).value,
                memberSummary = model.memberBillSummary.collectAsState(emptyList()).value,
                onClickMember = {
                    if (model.selectedMember == it) {
                        model.selectedMember = null
                    } else {
                        model.selectedMember = it
                    }
                },
                selectedMember = model.selectedMember,
                onClickBill = {
                    navigator.push(
                        InputResultScreen(it)
                    )
                },
                name = trip?.name ?: "Trip",
                onClickBack = {
                    navigator.pop()
                },
                onClickDelete = {
                    showDeleteDialog = true
                },
                tripModel = trip,
                showDeleteDialog = showDeleteDialog,
                onDismissDeleteDialog = {
                    showDeleteDialog = false
                },
                onConfirmDelete = {
                    showDeleteDialog = false
                    model.delete(navigator)
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScreenWidget(
    name: String = "Trip",
    tripData: List<BillModel>,
    memberSummary: List<TripScreenModel.MemberSummary>,
    selectedMember: String? = null,
    tripModel: TripModel? = null,
    showDeleteDialog: Boolean = false,
    onClickMember: (String) -> Unit = {},
    onClickBill: (String) -> Unit = {},
    onClickBack: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onDismissDeleteDialog: () -> Unit = {},
    onConfirmDelete: () -> Unit = {},
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(name)
            },
            actions = {
                IconButton(onClick = onClickDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            },
            navigationIcon = {
                IconButton(onClick = onClickBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) {
        Column(
            Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            Text(
                "Member Summary",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = Spacing.m)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                modifier = Modifier.horizontalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.m)
            ) {
                memberSummary.fastForEach {
                    Card(
                        onClick = { onClickMember(it.id) },
                        border = BorderStroke(
                            animateDpAsState(if (selectedMember == it.id) 2.dp else 0.dp).value,
                            animateColorAsState(
                                if (selectedMember == it.id) MaterialTheme.colorScheme.primary else Color.Transparent
                            ).value,
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            modifier = Modifier.padding(Spacing.m),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            PeopleWidget(
                                it.name,
                                isShowLabel = false,
                                size = 32.dp
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
                                Text(it.name, style = MaterialTheme.typography.labelMedium)
                                Text(
                                    it.total.toReadableCurrency(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

            Text(
                "Bill for Trip",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = Spacing.m)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.m),
                modifier = Modifier.padding(horizontal = Spacing.m)
            ) {
                tripData.fastForEach {
                    SplitBillItem(it, onClick = { onClickBill(it.id) }, tripModel = tripModel)
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = {
                Text("Delete Trip")
            },
            text = {
                Text("Are you sure you want to delete this trip? This action cannot be undone.")
            },
            confirmButton = {
                Button(onClick = onDismissDeleteDialog) {
                    Text("Cancel")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onConfirmDelete,
                ) {
                    Text("Delete")
                }

            }
        )
    }
}