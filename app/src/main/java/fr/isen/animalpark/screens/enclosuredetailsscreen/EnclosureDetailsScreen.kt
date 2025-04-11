package fr.isen.animalpark.screens.enclosuredetailsscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.TextFieldValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fr.isen.animalpark.R
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.models.Review
import fr.isen.animalpark.models.User
import fr.isen.animalpark.tools.DialTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnclosureDetailsScreen(
    enclosure: Enclosure,
    eventHandler: (String) -> Unit,
    databaseReference: DatabaseReference,
    index: Int,
    closeActivityHandler: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    var mealTime by remember { mutableStateOf(enclosure.meal) }
    var reviews by remember {mutableStateOf(emptyList<Review>())}
    val context = LocalContext.current

    // Listen for real-time updates
    LaunchedEffect(Unit) {
        val reviewsRef = databaseReference.child("biomes")
            .child((enclosure.id_biomes.toInt() - 1).toString())
            .child("enclosures")
            .child(index.toString())
            .child("reviews")

        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedReviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                reviews = updatedReviews
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        closeActivityHandler()
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = context.getString(R.string.get_back),
                        modifier = Modifier.requiredSize(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text (
                    text = context.getString(R.string.enclosure_details_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

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
                    text = if (enclosure.isOpen) context.getString(R.string.open) else context.getString(R.string.closed),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (User.getCurrentUser()?.isAdmin == true) {
                Button(onClick = {
                    showDialog = true
                }) {
                    Text(context.getString(R.string.modify_feeding_time))
                }

                if (showDialog) {
                    DialTimePicker(
                        onConfirm = { time ->
                            selectedTime = time
                            showDialog = false
                            if (selectedTime != null) {
                                databaseReference.child("biomes")
                                    .child((enclosure.id_biomes.toInt() - 1).toString())
                                    .child("enclosures").child(index.toString()).child("meal")
                                    .setValue("${selectedTime?.hour}:${selectedTime?.minute}")
                                mealTime = "${selectedTime?.hour}:${selectedTime?.minute}"
                            }
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = context.getString(R.string.animals),
                style =  MaterialTheme.typography.titleMedium,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (enclosure.animals.isNullOrEmpty()) {
                Text(context.getString(R.string.No_animals))
            } else {
                enclosure.animals.forEach { animal ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text =animal.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 20.sp
                        )
                        IconButton(
                            onClick = {
                                eventHandler(animal.name)
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.plus),
                                contentDescription = context.getString(R.string.details),
                                modifier = Modifier.requiredSize(24.dp),
                                colorFilter = ColorFilter.tint(colorResource(id = R.color.purple_80))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = context.getString(R.string.leave_a_review),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            var reviewText by remember { mutableStateOf(TextFieldValue("")) }
            var selectedStars by remember { mutableStateOf(0) }

            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(context.getString(R.string.your_review)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= selectedStars) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $star",
                        modifier = Modifier
                            .requiredSize(32.dp)
                            .clickable { selectedStars = star },
                        tint = if (star <= selectedStars) Color(0xFFFFD700) else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val reviewMap = mapOf(
                        "rating" to selectedStars,
                        "comment" to reviewText.text
                    )
                    reviewText = TextFieldValue("")
                    selectedStars = 0

                    databaseReference.child("biomes")
                        .child((enclosure.id_biomes.toInt() - 1).toString())
                        .child("enclosures")
                        .child(index.toString())
                        .child("reviews")
                        .push()
                        .setValue(reviewMap)
                          },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(context.getString(R.string.send_review))
            }
            reviews.forEach { review ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Star $star",
                                modifier = Modifier.requiredSize(24.dp),
                                tint = if (star <= review.rating) Color(0xFFFFD700) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = review.comment,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp
                    )
                }
            }
        }
        }
    }


