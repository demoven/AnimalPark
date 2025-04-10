package fr.isen.animalpark.screens.biomelistscreen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import  androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import fr.isen.animalpark.R

import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.models.User


@Composable
fun BiomeListScreen(biomes: List<Biome>, databaseReference: DatabaseReference, enclosureDetailsHandler:(Enclosure, Int) -> Unit, innerPadding: PaddingValues) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.padding(
            bottom = innerPadding.calculateBottomPadding()
        ),
    ) {
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
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                )

                if (expanded) {
                    // Show Enclosures
                    biome.enclosures.forEach { enclosure ->
                        var isOpen by remember { mutableStateOf(enclosure.isOpen) }
                        var mealTime by remember { mutableStateOf(enclosure.meal) }

                        val enclosureRef = databaseReference.child("biomes")
                            .child((enclosure.id_biomes.toInt() - 1).toString())
                            .child("enclosures")
                            .child(biome.enclosures.indexOf(enclosure).toString())

                        enclosureRef.child("meal").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                mealTime = snapshot.getValue(String::class.java) ?: enclosure.meal
                                enclosure.meal = mealTime
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Database", "Failed to read meal time", error.toException())
                            }
                        })

                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .fillMaxWidth()
                            //Border color red*
                                .border(
                                    BorderStroke(2.dp, if (isOpen) Color.Transparent else Color.Red),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.meal),
                                            contentDescription = context.getString(R.string.meal),
                                            modifier = Modifier
                                                .requiredSize(24.dp)
                                                .padding(end = 4.dp)
                                        )
                                        Text(
                                            text = mealTime,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Text(
                                        text = if (isOpen) context.getString(R.string.open) else context.getString(R.string.closed),
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontSize = 20.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
//
                                if (enclosure.animals.isNullOrEmpty()) {
                                    Text(text = context.getString(R.string.No_animals))
                                } else {
                                    enclosure.animals.forEach { animal ->
                                        Text(
                                            text = animal.name,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontSize = 20.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row {
                                    // Button to View Enclosure Details
                                    Button(
                                        onClick = {
                                            enclosureDetailsHandler(enclosure, biome.enclosures.indexOf(enclosure))
                                        }
                                    ) {
                                        Text(context.getString(R.string.see_details))
                                    }
                                    Spacer(
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )

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
                                            Text(if (enclosure.isOpen) context.getString(R.string.close_enclosure) else context.getString(R.string.open_enclosure))
                                        }
                                    }
                                }

                            }
                        }
                    }

                    // Show Services
                    if (biome.services.isNullOrEmpty()) {
                        Text(text = context.getString(R.string.no_services), modifier = Modifier.padding(start = 8.dp))
                    } else {
                        Text(
                            text = context.getString(R.string.services),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp),
                            fontSize = 20.sp
                        )

                        biome.services.forEach { service ->
                            Row {
                                Image(
                                    painter = if(service.availability) painterResource(id = R.drawable.check) else painterResource(id = R.drawable.xmark),
                                    contentDescription = context.getString(R.string.service_availability),
                                    modifier = Modifier
                                        .requiredSize(24.dp)
                                        .padding(end = 4.dp),
                                    colorFilter = if(service.availability)  ColorFilter.tint(colorResource(id = R.color.green)) else ColorFilter.tint(colorResource(id = R.color.red))
                                )
                                Text(
                                    text = service.name.replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
