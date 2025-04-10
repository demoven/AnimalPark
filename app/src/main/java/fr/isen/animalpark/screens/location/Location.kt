package fr.isen.animalpark.screens.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.animalpark.R
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.tools.findPath

@Composable
fun Location(enclosures: List<Enclosure>, innerPadding: PaddingValues) {
    val context = LocalContext.current
    var startId by remember { mutableStateOf<Enclosure?>(null) }
    var endId by remember { mutableStateOf<Enclosure?>(null) }
    var path by remember { mutableStateOf<List<String>?>(null) }

    Column(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = context.getString(R.string.location),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 28.sp,
            )
        }

        DropdownSelector(
            label = context.getString(R.string.start_location),
            options = enclosures,
            selected = startId
        ) {
            startId = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector(
            label = context.getString(R.string.end_location),
            options = enclosures,
            selected = endId
        ) {
            endId = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                path = findPath(
                    enclosures = enclosures,
                    startId = startId,
                    endId = endId,
                    context = context
                )
            },
            modifier = Modifier
                .padding(start = 28.dp)
        ) {
            Text(context.getString(R.string.calculate_route))
        }

        Spacer(modifier = Modifier.height(24.dp))

        path?.let {
            Column(modifier = Modifier.padding(start = 28.dp)) {
                Text(
                    text = context.getString(R.string.route),
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (it.isNotEmpty()) {
                    for ((index, enclosure) in it.withIndex()) {
                        Row {
                            Text(
                                text = "${index + 1}. $enclosure",
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 20.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                } else {
                    Text("No path found", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<Enclosure>,
    selected: Enclosure?,
    onSelect: (Enclosure) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 28.dp)
    ) {

        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 20.sp,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            if (selected != null) {
                if (selected.animals.isNullOrEmpty()) {
                    Text(context.getString(R.string.error_no_animals))
                } else {
                    Text(selected.animals[0].name)
                }
            } else {
                Text(context.getString(R.string.select_enclosure))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 350.dp)

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        if (option.animals.isNullOrEmpty()) {
                            Text(context.getString(R.string.error_no_animals))
                        } else {
                            Text(option.animals[0].name)
                        }
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
