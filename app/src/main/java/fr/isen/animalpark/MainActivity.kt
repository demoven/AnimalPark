package fr.isen.animalpark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.models.User
import fr.isen.animalpark.screens.main.MainScreen
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var biomelist by mutableStateOf<List<Biome>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
        retrieveDataFromDatabase()
//        getUserDataFromDatabase()
        enableEdgeToEdge()
        setContent {
            MainScreen(
                biomes = biomelist,
                databaseReference = databaseReference,
                signOutHandler = {
                    auth.signOut()
                    startLoginActivity()
                },
                enclosureDetailsHandler = { enclosure, indexEnclosure ->
                    startEnclosureDetailsActivity(enclosure, indexEnclosure)
                },
                deleteAccountHandler = {
                    deleteAccount()
                },
                user = user,
                callHandler = {
                    callEmergency()
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            user = currentUser
            getUserDataFromDatabase()
        } else {
            startLoginActivity()
        }
    }

    fun parseBiomesFromJson(jsonString: String): List<Biome> {
        val gson = Gson()
        val jsonArray = JSONObject(jsonString).getJSONArray("biomes").toString()
        val type = object : TypeToken<List<Biome>>() {}.type
        return gson.fromJson(jsonArray, type)
    }

    fun getUserDataFromDatabase() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            databaseReference.child("users").child(userId).child("isAdmin")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val isAdmin = snapshot.value as Boolean
                        User.setCurrentUser(User(userId, isAdmin))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Firebase", "Erreur de chargement: ${error.message}")
                    }
                })
        }
    }

    private fun retrieveDataFromDatabase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val json = Gson().toJson(snapshot.value)
                biomelist = parseBiomesFromJson(json)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Erreur de chargement: ${error.message}")
            }
        })
    }

    private fun startLoginActivity() {
        //loggout the user
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startEnclosureDetailsActivity(enclosure: Enclosure, indexEnclosure: Int) {
        val intent = Intent(this, EnclosureDetailsActivity::class.java)
        intent.putExtra(EnclosureDetailsActivity.ENCLOSURE_KEY, enclosure)
        intent.putExtra(EnclosureDetailsActivity.ECLOSURE_INDEX, indexEnclosure)
        startActivity(intent)
    }

    private fun callEmergency() {
        val phoneNumber = "0123456789" // Replace with the actual emergency number
        val u = Uri.parse("tel:$phoneNumber")
        val i = Intent(Intent.ACTION_DIAL, u)
        try {
            startActivity(i)
        } catch (e: Exception) {
            Log.d("MainActivity", "Error: $e")
        }
    }


    private fun deleteAccount() {
        val user = auth.currentUser
        if (user != null) {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "User account deleted.")
                        startLoginActivity()
                    } else {
                        Log.d(
                            "Firebase",
                            "Failed to delete user account: ${task.exception?.message}"
                        )
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