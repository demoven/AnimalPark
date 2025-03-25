package fr.isen.animalpark.screens.enclosuredetailsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.animalpark.models.Enclosure

@Composable
fun EnclosureDetailsScreen(enclosure: Enclosure) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Enclosure ID: ${enclosure.id}",
            style = MaterialTheme.typography.titleLarge
        )
        Text("Meal Type: ${enclosure.meal}")
        Text("Open: ${if (enclosure.isOpen) "Yes" else "No"}")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Animals:", style = MaterialTheme.typography.titleMedium)
        if (enclosure.animals.isNullOrEmpty()) {
            Text("No animals in this enclosure")
        } else {
            enclosure.animals.forEach { animal ->
                Text("- ${animal.name} (ID: ${animal.id})")
            }
        }
    }
}
