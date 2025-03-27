package fr.isen.animalpark

import android.content.Intent
import android.net.Uri

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
import fr.isen.animalpark.screens.biomelistscreen.BiomeListScreen
import fr.isen.animalpark.models.User
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
        databaseReference = firebaseDatabase.getReference()
        retrieveDataFromDatabase()
        getUserDataFromDatabase()
        enableEdgeToEdge()
        setContent {
            AnimalParkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (biomelist.isNotEmpty()) {
                        BiomeListScreen(
                            biomes = biomelist,
                            modifier = Modifier.padding(innerPadding),
                            databaseReference = databaseReference
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
            getUserDataFromDatabase()
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

    fun getUserDataFromDatabase(){
        val user = auth.currentUser
        if (user != null){
            val userId = user.uid
            databaseReference.child("users").child(userId).child("isAdmin").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    val isAdmin = snapshot.value as Boolean
                    User.setCurrentUser(User(userId, isAdmin))
                    if (User.getCurrentUser()?.isAdmin == true)
                    {
                        Log.d("YEEEEEEEEE", "IsAdmin")
                    }
                    else
                    {
                        Log.d("USSSSSEEEEERRRR", "IsNotAdmin")
                    }
                    if (isAdmin)
                        Log.d("USSSSSEEEEERRRR", "IsAdmin")
                    else
                        Log.d("USSSSSEEEEERRRR", "IsNotAdmin")
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", "Erreur de chargement: ${error.message}")
                }
            })
        }
    }

    private fun retrieveDataFromDatabase(){
        databaseReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val json = Gson().toJson(snapshot.value)
                 biomelist = parseBiomesFromJson(json)
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
fun openDialer(){
    val phoneNumber = "0123456789"
    val context = LocalContext.current
    Button(
        modifier = Modifier.padding(top = 150.dp),
        onClick = {
            val u = Uri.parse("tel:$phoneNumber")
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                context.startActivity(i)
            } catch (e: Exception) {
                Log.d("MainActivity", "Error: $e")
            }
        }
    ){
        Text("Call the zoo")
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

