package fr.isen.animalpark.screens.profile

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.events.EventHandler
import fr.isen.animalpark.LoginActivity
import fr.isen.animalpark.MainActivity
import fr.isen.animalpark.R

@Composable
fun ProfileScreen(signOutHandler: ()-> Unit) {
    var newPassword = remember { mutableStateOf("") }
    var oldPassword = remember { mutableStateOf("") }
    var deleteAccount = remember {mutableStateOf(false)}
    Column () {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = {
                    signOutHandler()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Sign Out")
            }
        }
        Text("Change Password", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp))
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .border(
                    BorderStroke(width = 2.dp, color = Color.Black),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            value = oldPassword.value,
            onValueChange = {
                oldPassword.value = it
            },
            placeholder = {Text("Old Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .border(
                    BorderStroke(width = 2.dp, color = Color.Black),
                    shape = RoundedCornerShape(50)
                ),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            value = newPassword.value,
            onValueChange = {
                newPassword.value = it
            },
            placeholder = {Text("New password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = {

            },
        ) {
            Text(
                text = "Change Password",
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }
        Text("Supprimer le compte", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = {

            },
        ) {
            Text(
                text = "Supprimer le compte",
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }
    }
}

