package fr.isen.animalpark.screens.enclosuredetailsscreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DatabaseReference
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.models.User
import fr.isen.animalpark.tools.DialTimePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnclosureDetailsScreen(enclosure: Enclosure, eventHandler: (String) -> Unit, databaseReference: DatabaseReference, index: Int) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    var mealTime by remember { mutableStateOf(enclosure.meal) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Enclosure ID: ${enclosure.id}",
            style = MaterialTheme.typography.titleLarge
        )
        Text("Meal Type: $mealTime")
        Text("Open: ${if (enclosure.isOpen) "Yes" else "No"}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            showDialog = true
        }){
            Text("Modifier l'heure de repas")
        }

        if (showDialog && User.getCurrentUser()?.isAdmin == true) {
            DialTimePicker(
                onConfirm = { time ->
                    selectedTime = time
                    showDialog = false
                    Log.d(
                        "TimePicker",
                        "Selected time: ${selectedTime?.hour}:${selectedTime?.minute}"
                    )
                    if (selectedTime != null ){
                        databaseReference.child("biomes").child((enclosure.id_biomes.toInt() - 1).toString()).child("enclosures").child(index.toString()).child("meal").setValue("${selectedTime?.hour}:${selectedTime?.minute}")
                        mealTime = "${selectedTime?.hour}:${selectedTime?.minute}"
                    }
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }

        Text("Animals:", style = MaterialTheme.typography.titleMedium)
        if (enclosure.animals.isNullOrEmpty()) {
            Text("No animals in this enclosure")
        } else {
            enclosure.animals.forEach { animal ->
                Text(
                    modifier = Modifier.clickable { eventHandler(animal.name) },
                    text = "- ${animal.name} (ID: ${animal.id})"
                )
            }
        }

    }

}

