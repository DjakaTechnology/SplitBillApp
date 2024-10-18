package id.djaka.splitbillapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.readableDateYearFormat
import id.djaka.splitbillapp.widget.DatePickerPopUpWidget
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.format

class HomeAddTripSheetState {
    var name by mutableStateOf("")
    var startDate by mutableStateOf(Clock.System.now().toEpochMilliseconds())
    var endDate by mutableStateOf(Clock.System.now().toEpochMilliseconds())

    var isSelectingStartDate by mutableStateOf(false)
    var isSelectingEndDate by mutableStateOf(false)
}

@Composable
fun rememberHomeAddTripSheetState(
    name: String = "",
    startDate: Long = Clock.System.now().toEpochMilliseconds(),
    endDate: Long = Clock.System.now().toEpochMilliseconds(),
    isSelectingStartDate: Boolean = false,
    isSelectingEndDate: Boolean = false
): HomeAddTripSheetState {
    return HomeAddTripSheetState().apply {
        this.name = name
        this.startDate = startDate
        this.endDate = endDate
        this.isSelectingStartDate = isSelectingStartDate
        this.isSelectingEndDate = isSelectingEndDate
    }
}

@Composable
fun HomeAddTripSheet(
    modifier: Modifier = Modifier,
    state: HomeAddTripSheetState = rememberHomeAddTripSheetState(),
    onClickSave: () -> Unit
) {
    if (state.isSelectingStartDate) {
        DatePickerPopUpWidget(
            date = state.startDate,
            onDateChange = { state.startDate = it },
            onDismissRequest = { state.isSelectingStartDate = false }
        )
    } else if (state.isSelectingEndDate) {
        DatePickerPopUpWidget(
            date = state.endDate,
            onDateChange = { state.endDate = it },
            onDismissRequest = { state.isSelectingEndDate = false }
        )
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { state.name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Instant.fromEpochMilliseconds(state.startDate).format(readableDateYearFormat),
            onValueChange = { },
            label = { Text("Start Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { state.isSelectingStartDate = !state.isSelectingStartDate }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            }
        )

        OutlinedTextField(
            value = Instant.fromEpochMilliseconds(state.endDate).format(readableDateYearFormat),
            onValueChange = { },
            label = { Text("End Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { state.isSelectingEndDate = !state.isSelectingEndDate }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            }
        )

        Button(onClick = onClickSave, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}