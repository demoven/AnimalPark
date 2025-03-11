package fr.isen.animalpark.screens.sign_in

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import fr.isen.animalpark.LoginActivity
import fr.isen.animalpark.MainActivity
import fr.isen.animalpark.RegisterActivity

@Composable
fun SignInScreen(auth: FirebaseAuth) {

    //Use mutableStateOf to store the email and password
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val context = LocalContext.current


    Column(modifier = Modifier.padding(top = 46.dp)) {
        OutlinedTextField(
            singleLine = true,
            value = email.value,
            onValueChange = {
                email.value = it
            },
            placeholder = { Text("Email") }
        )
        OutlinedTextField(
            singleLine = true,
            value = password.value,
            onValueChange = {
                password.value = it
            },
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (email.value == "" || password.value == "") {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success")
                            //Redirect to the main activity
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            //Finish the current activity
                            (context as LoginActivity).finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("Login", "signInWithEmail:failure", task.exception)
                            //Toast login failed
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
        ) {
            Text("Sign In")
        }
        TextButton(
            onClick = {
                //Redirect to the register activity
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            }
        ) {
           Text("Create an account")
        }
    }
}
