package fr.isen.animalpark.tools

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import fr.isen.animalpark.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
){
    val context = LocalContext.current
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    Column {
        TimeInput(state = timePickerState)
        Button(onClick = onDismiss) {
            Text(context.getString(R.string.cancel))
        }
        Button (onClick = { onConfirm(timePickerState) }) {
            Text(context.getString(R.string.confirm))
        }
    }
}