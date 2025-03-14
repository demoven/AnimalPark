package fr.isen.animalpark


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.screens.biomelistscreen.BiomeListScreen
import fr.isen.animalpark.ui.theme.AnimalParkTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var biomelist by mutableStateOf<List<Biome>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        Log.d("Firebase", firebaseDatabase.toString())
        databaseReference = firebaseDatabase.getReference()
        Log.d("Database", databaseReference.toString())
        retrieveDataFromDatabase()
        Log.d("Database", "Data retrieved")
        enableEdgeToEdge()
        setContent {
            AnimalParkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (biomelist.isNotEmpty()) {
                        BiomeListScreen(
                            biomes = biomelist,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        Text("Chargement des biomes...", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
    fun parseBiomesFromJson(jsonString: String): List<Biome> {
        val gson = Gson()
        val jsonArray = JSONObject(jsonString).getJSONArray("biomes").toString()
        val type = object : TypeToken<List<Biome>>() {}.type
        return gson.fromJson(jsonArray, type)
    }

    private fun retrieveDataFromDatabase(){
        databaseReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val json = Gson().toJson(snapshot.value)
                 biomelist = parseBiomesFromJson(json)
                for (enclosure in snapshot.children){
                    Log.d("Enclosure", enclosure.toString())


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Erreur de chargement: ${error.message}")
            }
        })
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimalParkTheme {
        Greeting("Android")
    }
}