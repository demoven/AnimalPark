package fr.isen.animalpark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.animalpark.ui.theme.AnimalParkTheme

class AnimalDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalName = intent.getSerializableExtra(AnimalDetailsActivity.animalExtraKey) as? String
        enableEdgeToEdge()
        setContent {
            AnimalParkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (animalName != null) {
                        Greeting2(
                            name = animalName,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }

    }

    companion object{
        val animalExtraKey = "animalExtraKey"
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimalParkTheme {
        Greeting2("Android")
    }
}