package fr.isen.animalpark.tools

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


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
