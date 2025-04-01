package fr.isen.animalpark

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.animalpark.ui.theme.AnimalParkTheme
import kotlinx.coroutines.launch

class AnimalDetailsActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalName = intent.getSerializableExtra(AnimalDetailsActivity.animalExtraKey) as? String
        enableEdgeToEdge()
        setContent {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = "AIzaSyDdrM0fHqVaolZSadXvI0qcbCs-kHh-4ck"
            )
            val coroutinScope = rememberCoroutineScope()
            val prompt = "Donne moi une courte description de l'animal $animalName"
            var response by remember { mutableStateOf<String?>(null) }
            AnimalParkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (animalName != null) {
                        LaunchedEffect(animalName) {
                            if (response == null && animalName != null) {
                                response = generativeModel.generateContent(prompt).text.toString()
                            }
                        }
                    }
                    response?.let {
                        displayInfo(
                            name = it,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    companion object {
        val animalExtraKey = "animalExtraKey"
    }

}

@Composable
fun displayInfo(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimalParkTheme {
    }
}