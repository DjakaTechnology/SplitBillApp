package id.djaka.splitbillapp.input.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.util.readableDateFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format

class InputResultDetailWidgetState {
    var name: String by mutableStateOf("")
    var date: Long by mutableStateOf(Clock.System.now().toEpochMilliseconds())
    var showDatePicker: Boolean by mutableStateOf(false)
}

@Composable
fun rememberInputResultDetailWidgetState(
    name: String = "",
    date: Long = Clock.System.now().toEpochMilliseconds(),
    showDatePicker: Boolean = false
): InputResultDetailWidgetState {
    return remember(name, date, showDatePicker) {
        InputResultDetailWidgetState().apply {
            this.name = name
            this.date = date
            this.showDatePicker = showDatePicker
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
        Button(onClick = onClickSave, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerInput(state: InputResultDetailWidgetState) {
    OutlinedTextField(
        value = Instant.fromEpochMilliseconds(state.date).format(readableDateFormat),
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
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.date)
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            state.date = it
        }
    }
    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { state.showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.date = datePickerState.selectedDateMillis ?: state.date
                    state.showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    state.showDatePicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}