package fr.isen.animalpark.screens.animaldetailsscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.animalpark.R

@Composable
fun AnimalDetailsScreen(animalName: String, description: String, closeActivityHandler: () -> Unit) {
    val context = LocalContext.current
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        closeActivityHandler()
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = context.getString(R.string.get_back),
                        modifier = Modifier.requiredSize(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text (
                    text = animalName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = context.getString(R.string.description),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
            )
        }
    }
}