package fr.isen.animalpark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.screens.enclosuredetailsscreen.EnclosureDetailsScreen

class EnclosureDetailsActivity : ComponentActivity() {

    companion object {
        val ENCLOSURE_KEY = "enclosure_key"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enclosure = intent.getSerializableExtra(ENCLOSURE_KEY) as? Enclosure

        setContent {
            MaterialTheme {
                if (enclosure != null) {
                    EnclosureDetailsScreen(enclosure = enclosure, eventHandler = { animalName ->
                        startAnimalDetailsActivity(animalName)
                    })
                } else {
                    Text("Enclosure not found")
                }
            }
        }
    }

    private fun startAnimalDetailsActivity(animalName: String) {
        val intent = Intent(this, AnimalDetailsActivity::class.java).apply{
            putExtra(AnimalDetailsActivity.animalExtraKey, animalName)
        }
        startActivity(intent)
    }
}
