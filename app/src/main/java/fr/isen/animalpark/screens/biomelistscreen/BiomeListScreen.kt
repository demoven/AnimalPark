package fr.isen.animalpark.screens.biomelistscreen

import android.content.Intent
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.DatabaseReference
import fr.isen.animalpark.EnclosureDetailsActivity
import fr.isen.animalpark.R

import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.models.User


@Composable
fun BiomeListScreen(biomes: List<Biome>, modifier: Modifier = Modifier, databaseReference: DatabaseReference) {
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
                // Display Biome Name
                Text(
                    text = biome.name,
                    color = Color(android.graphics.Color.parseColor(biome.color)),
                    style = MaterialTheme.typography.titleLarge
                )

                if (expanded) {
                    // Show Enclosures
                    biome.enclosures.forEach { enclosure ->
                        var isOpen by remember { mutableStateOf(enclosure.isOpen) }
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "${context.getString(R.string.meal)}: ${enclosure.meal}")
                                Text(text = "${context.getString(R.string.open)}: ${if (isOpen) context.getString(R.string.yes) else context.getString(R.string.no)}")

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${context.getString(R.string.animals)}:",
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

                                Spacer(modifier = Modifier.height(8.dp))

                                // Button to View Enclosure Details
                                Button(
                                    onClick = {
                                        val intent = Intent(context, EnclosureDetailsActivity::class.java)
                                        intent.putExtra(EnclosureDetailsActivity.ENCLOSURE_KEY, enclosure)
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Text("Voir les détails")
                                }

                                // Admin Toggle Button
                                if (User.getCurrentUser()?.isAdmin == true) {
                                    Button(
                                        onClick = {
                                            val biomeId = enclosure.id_biomes.toInt() - 1
                                            val enclosureIndex = biome.enclosures.indexOfFirst { it.id == enclosure.id }
                                            enclosure.isOpen = !enclosure.isOpen
                                            isOpen = enclosure.isOpen
                                            val updates = mapOf("isOpen" to enclosure.isOpen)
                                            databaseReference.child("biomes").child(biomeId.toString()).child("enclosures").child(enclosureIndex.toString()).updateChildren(updates)
                                        }
                                    ) {
                                        Text("Toggle")
                                    }
                                }
                            }
                        }
                    }

                    // Show Services
                    if (biome.services.isNotEmpty()) {
                        Text(
                            text = "Services:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        biome.services.forEach { service ->
                            Text(
                                text = "- ${service.name} (Available: ${if (service.availability) "Yes" else "No"})",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    } else {
                        Text(text = "No services available in this biome.", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}
