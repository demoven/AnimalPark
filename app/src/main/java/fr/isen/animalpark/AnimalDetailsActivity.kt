package fr.isen.animalpark

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.animalpark.screens.animaldetailsscreen.AnimalDetailsScreen
import fr.isen.animalpark.ui.theme.AnimalParkTheme

class AnimalDetailsActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalName = intent.getSerializableExtra(animalExtraKey) as? String
        enableEdgeToEdge()
        setContent {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = "AIzaSyDdrM0fHqVaolZSadXvI0qcbCs-kHh-4ck"
            )
            val prompt = "${getString(R.string.prompt)} $animalName"
            var response by remember { mutableStateOf<String?>(null) }
                    if (animalName != null) {
                        LaunchedEffect(animalName) {
                            if (response == null && animalName != null) {
                                response = generativeModel.generateContent(prompt).text.toString()
                            }
                        }
                    }
                    response?.let {
                        if (animalName != null) {
                            AnimalDetailsScreen(
                                animalName = animalName,
                                description = it,
                                closeActivityHandler = {
                                    closeActivity()
                                }
                            )
                        }
                    }
        }
    }
    private fun closeActivity() {
        finish()
    }

    companion object {
        val animalExtraKey = "animalExtraKey"
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimalParkTheme {
    }
}