package fr.isen.animalpark.screens.navigation

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.tools.findPath

@Composable
fun Location(enclosures: List<Enclosure>) {
    var startId by remember { mutableStateOf("") }
    var endId by remember { mutableStateOf("") }
    var path by remember { mutableStateOf<List<String>?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Find Path Between Enclosures", style = MaterialTheme.typography.titleLarge)

        DropdownSelector("Start Enclosure", enclosures.map { it.id }, startId) { startId = it }
        Spacer(modifier = Modifier.height(8.dp))
        DropdownSelector("End Enclosure", enclosures.map { it.id }, endId) { endId = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            path = findPath(enclosures, startId, endId)
        }) {
            Text("Find Path")
        }

        Spacer(modifier = Modifier.height(16.dp))

        path?.let {
            if (it.isNotEmpty()) {
                Text("Path: ${it.joinToString(" → ")}")
            } else {
                Text("No path found", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(text = selected.ifEmpty { "Select..." })
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
