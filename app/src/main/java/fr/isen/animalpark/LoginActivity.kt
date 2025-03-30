package fr.isen.animalpark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.animalpark.models.User
import fr.isen.animalpark.screens.sign_in.SignInScreen

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        //Check if the user is already logged in
        if (auth.currentUser != null) {
            User.setCurrentUser(User(auth.currentUser!!.uid))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        enableEdgeToEdge()
        setContent {
            SignInScreen(auth)
        }
    }

    override fun onStart() {
        super.onStart()
        User.setCurrentUser(User("", false))
    }
}


