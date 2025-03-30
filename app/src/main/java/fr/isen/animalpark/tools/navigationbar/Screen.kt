package fr.isen.animalpark.tools.navigationbar

sealed class Screen(val rout: String) {
    data object Home: Screen("biome_list_screen")
    data object Profile: Screen("profile_screen")
    data object Location: Screen("location_screen")
}