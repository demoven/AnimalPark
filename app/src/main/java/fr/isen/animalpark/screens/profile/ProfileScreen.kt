package fr.isen.animalpark.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import fr.isen.animalpark.R

@Composable
fun ProfileScreen(signOutHandler: ()-> Unit, deleteAccountHandler: () -> Unit, user: FirebaseUser) {
    val newPassword = remember { mutableStateOf("") }
    val oldPassword = remember { mutableStateOf("") }
    val deleteAccount = remember {mutableStateOf(false)}
    val context = LocalContext.current

    Column {
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
                Text(context.getString(R.string.sign_out))
            }
        }
        Text(context.getString(R.string.change_password), style = MaterialTheme.typography.titleLarge,
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
            placeholder = {Text(context.getString(R.string.old_password)) },
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
            placeholder = {Text(context.getString(R.string.new_password)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = {
                val oldPasswordValue = oldPassword.value
                val newPasswordValue = newPassword.value
                oldPassword.value = ""
                newPassword.value = ""

                if (oldPasswordValue.isNullOrEmpty() || newPasswordValue.isNullOrEmpty()) {
                    Toast.makeText(context, context.getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val credential = EmailAuthProvider.getCredential(user.email!!, oldPasswordValue)
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPasswordValue).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Log.d("Firebase", "Password updated successfully.")
                                Toast.makeText(context, context.getString(R.string.password_update_success), Toast.LENGTH_SHORT).show()

                            } else {
                                Log.d("Firebase", "Failed to update password: ${updateTask.exception?.message}")
                                Toast.makeText(context, context.getString(R.string.password_update_failure), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.d("Firebase", "Old password is incorrect: ${task.exception?.message}")
                        Toast.makeText(context, context.getString(R.string.old_password_incorrect), Toast.LENGTH_SHORT).show()
                    }
                }
            },
        ) {
            Text(
                text = context.getString(R.string.modify),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(context.getString(R.string.delete_account), style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = {
                deleteAccount.value = true
            },
        ) {
            Text(
                text = context.getString(R.string.delete),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }
        if (deleteAccount.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                ,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        deleteAccount.value = false
                    }
                ) {
                    Text(context.getString(R.string.cancel))
                }
                TextButton(
                    onClick = {
                        deleteAccountHandler()
                        deleteAccount.value = false
                    }
                ) {
                    Text(context.getString(R.string.confirm))
                }
            }
        }
    }
}

