package fr.isen.animalpark

import android.content.Intent

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.isen.animalpark.models.Enclosure
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.ui.theme.AnimalParkTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var biomelist by mutableStateOf<List<Biome>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        Log.d("Firebase", firebaseDatabase.toString())
        databaseReference = firebaseDatabase.getReference()
        retrieveDataFromDatabase()
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
           buttonSignOut(auth)
        }
    }}

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "OnStart")
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("User", currentUser.toString())
            currentUser.let {
                Log.d("User email", it.email.toString())
            }
        } else {
            //Launch the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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
fun buttonSignOut(auth: FirebaseAuth){
    //Button to sign out
    val context = LocalContext.current
    Button(
        onClick = {
            auth.signOut()
            //Redirect to the login activity
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            // Finish the current activity
//            (context as MainActivity).finish()
        },
        modifier = Modifier.padding(top = 48.dp)
    ) {
        Text("Sign Out")
    }
}
@Composable
fun BiomeListScreen(biomes: List<Biome>,modifier: Modifier = Modifier) {
    LazyColumn {
        items(biomes) { biome ->
            Text(
                text = biome.name,
                color = Color(android.graphics.Color.parseColor(biome.color)),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )

            biome.enclosures.forEach { enclosure ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Enclosure ID: ${enclosure.id}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Meal: ${enclosure.meal}")
                        Text(text = "Open: ${if (enclosure.isOpen) "Yes" else "No"}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Animals:",
                            style = MaterialTheme.typography.labelLarge
                        )

                        if (enclosure.animals.isNullOrEmpty()) {
                            Text(text = "No animals in this enclosure")
                        } else {
                            enclosure.animals?.forEach { animal ->
                            Text(
                                    text = "- ${animal.name} (ID: ${animal.id})",
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