package id.djaka.splitbillapp.input.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.readableDateYearFormat
import id.djaka.splitbillapp.widget.DatePickerPopUpWidget
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.format

class InputResultDetailWidgetState {
    var name: String by mutableStateOf("")
    var date: Long by mutableStateOf(Clock.System.now().toEpochMilliseconds())
    var showDatePicker: Boolean by mutableStateOf(false)
    var tripList: List<String> by mutableStateOf(emptyList())
    var selectedTripIndex: Int by mutableStateOf(0)
}

@Composable
fun rememberInputResultDetailWidgetState(
    name: String = "",
    date: Long = Clock.System.now().toEpochMilliseconds(),
    showDatePicker: Boolean = false,
    tripList: List<String> = emptyList(),
    selectTripIndex: Int = 0,
): InputResultDetailWidgetState {
    return remember(name, date, showDatePicker, selectTripIndex) {
        InputResultDetailWidgetState().apply {
            this.name = name
            this.date = date
            this.showDatePicker = showDatePicker
            this.tripList = tripList
            this.selectedTripIndex = selectTripIndex
        }
    }
}

@Composable
fun InputResultDetailWidget(
    modifier: Modifier = Modifier,
    state: InputResultDetailWidgetState = rememberInputResultDetailWidgetState(),
    onClickSave: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.m), modifier = modifier) {
        Text("Bill Details", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = state.name,
            onValueChange = { state.name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        DatePickerInput(state)

        if (state.tripList.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = state.tripList.getOrNull(state.selectedTripIndex) ?: "",
                    onValueChange = { },
                    label = { Text("Trip") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select trip"
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    state.tripList.fastForEachIndexed { index, it ->
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                state.selectedTripIndex = index
                            },
                        )
                    }
                }
            }
        }
        Button(onClick = onClickSave, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}

@Composable
private fun DatePickerInput(state: InputResultDetailWidgetState) {
    OutlinedTextField(
        value = Instant.fromEpochMilliseconds(state.date).format(readableDateYearFormat),
        onValueChange = { },
        label = { Text("Date") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { state.showDatePicker = !state.showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        }
    )

    if (state.showDatePicker) {
        DatePickerPopUpWidget(
            date = state.date,
            onDateChange = { state.date = it },
            onDismissRequest = { state.showDatePicker = false }
        )
    }
}