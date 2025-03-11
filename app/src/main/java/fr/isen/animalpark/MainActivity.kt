package fr.isen.animalpark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.ui.theme.AnimalParkTheme

class MainActivity : ComponentActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        Log.d("Firebase", firebaseDatabase.toString())
        databaseReference = firebaseDatabase.getReference()
        Log.d("Database", databaseReference.toString())
        retrieveDataFromDatabase()
        Log.d("Database", "Data retrieved")
        enableEdgeToEdge()
        setContent {
           buttonSignOut(auth)
        }
    }

    override fun onStart() {
        super.onStart()
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

    private fun retrieveDataFromDatabase(){
        databaseReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                for (enclosure in snapshot.children){
                    Log.d("Enclosure", enclosure.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.d("Enclosure", "Error")
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
            (context as MainActivity).finish()
        },
        modifier = Modifier.padding(top = 48.dp)
    ) {
        Text("Sign Out")
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