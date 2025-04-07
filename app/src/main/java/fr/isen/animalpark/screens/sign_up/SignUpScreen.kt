package fr.isen.animalpark.screens.sign_up

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import fr.isen.animalpark.LoginActivity
import fr.isen.animalpark.MainActivity
import fr.isen.animalpark.R
import fr.isen.animalpark.RegisterActivity

@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    db: DatabaseReference,
    loginActivityHandler: () -> Unit,
    mainActivityHandler: () -> Unit
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = context.getString(R.string.logo),
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .size(130.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
        )

        //Email field
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
            value = email.value,
            onValueChange = {
                email.value = it
            },
            placeholder = { Text(context.getString(R.string.email)) }
        )

        //Password field
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
            value = password.value,
            onValueChange = {
                password.value = it
            },
            placeholder = { Text(context.getString(R.string.password)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        //Register button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = {
                //Empty fields check
                if (email.value == "" || password.value == "") {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_in_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                //Password length check
                if (password.value.length < 6) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_too_short),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                // Create a new user
                auth.createUserWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Sign in the user
                            auth.signInWithEmailAndPassword(email.value, password.value)
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        //Launch the main activity
                                        mainActivityHandler()
                                        if (auth.currentUser != null) {
                                            //Save the user in the database
                                            db.child("users").child(auth.currentUser!!.uid)
                                                .child("isAdmin").setValue(false)
                                        }
                                    } else {
                                        Log.d("Login", "signInWithEmail:failure", task2.exception)
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.failed_register),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                        } else {
                            Log.d("Register", "createUserWithEmail:failure", task.exception)
                        }
                    }
            },
        ) {
            Text(
                text = context.getString(R.string.register),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }

        //Cancel button
        TextButton(
            onClick = {
                //Launch the login activity
                loginActivityHandler()
            },
        ) {
            Text(context.getString(R.string.cancel))
        }

    }
}
