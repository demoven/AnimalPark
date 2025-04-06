package fr.isen.animalpark.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.google.firebase.database.DatabaseReference
import fr.isen.animalpark.models.Biome
import fr.isen.animalpark.models.Enclosure
import fr.isen.animalpark.screens.biomelistscreen.BiomeListScreen
import fr.isen.animalpark.screens.location.LocationScreen
import fr.isen.animalpark.screens.profile.ProfileScreen
import fr.isen.animalpark.tools.navigationbar.BottomNavigationBar
import fr.isen.animalpark.tools.navigationbar.Screen


@Composable
fun MainScreen(biomes: List<Biome>, databaseReference: DatabaseReference, signOutHandler: () -> Unit,  enclosureDetailsHandler:(Enclosure, Int) -> Unit) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->

        val graph =
            navController.createGraph(startDestination = Screen.Home.rout) {
                composable(route = Screen.Location.rout) {
                    LocationScreen()
                }
                composable(route = Screen.Home.rout) {
                    if (biomes.isNotEmpty()){
                        BiomeListScreen(
                            biomes = biomes,
                            databaseReference = databaseReference,
                            enclosureDetailsHandler = { enclosure, index ->
                                enclosureDetailsHandler(enclosure, index)
                            }
                        )
                    }
                    else {
                        Text("Chargement des biomes...", modifier = Modifier.padding(16.dp))
                    }

                }
                composable(route = Screen.Profile.rout) {
                    ProfileScreen(signOutHandler = signOutHandler)
                }
            }
        NavHost(
            navController = navController,
            graph = graph,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = 10.dp
            )
        )

    }
}