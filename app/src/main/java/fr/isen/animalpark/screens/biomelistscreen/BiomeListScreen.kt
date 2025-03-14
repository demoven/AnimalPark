package fr.isen.animalpark.screens.biomelistscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import fr.isen.animalpark.R

import fr.isen.animalpark.models.Biome


@Composable
fun BiomeListScreen(biomes: List<Biome>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LazyColumn(modifier = modifier) {
        items(biomes) { biome ->
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Text(
                    text = biome.name,
                    color = Color(android.graphics.Color.parseColor(biome.color)),
                    style = MaterialTheme.typography.titleLarge
                )

                if (expanded) {
                    biome.enclosures.forEach { enclosure ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = context.getString(R.string.meal)+": ${enclosure.meal}")
                                Text(text = context.getString(R.string.open)+": ${if (enclosure.isOpen) context.getString(R.string.yes)  else context.getString(R.string.no)}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = context.getString(R.string.animals)+":",
                                    style = MaterialTheme.typography.labelLarge
                                )

                                if (enclosure.animals.isNullOrEmpty()) {
                                    Text(text = context.getString(R.string.No_animals))
                                } else {
                                    enclosure.animals.forEach { animal ->
                                        Text(
                                            text = "- ${animal.name}",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

