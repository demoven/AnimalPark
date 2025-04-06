package fr.isen.animalpark.screens.profile

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.events.EventHandler
import fr.isen.animalpark.LoginActivity

@Composable
fun ProfileScreen(signOutHandler: ()-> Unit) {
    Column () {
        Button(
            onClick = {
                signOutHandler()
            }
        ) {
            Text("Sign Out")
        }
    }
}

