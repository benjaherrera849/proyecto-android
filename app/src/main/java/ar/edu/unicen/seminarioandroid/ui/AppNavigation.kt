package ar.edu.unicen.seminarioandroid.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// define las pantallas de la app y como se navega entre ellas
@Composable
fun AppNavigation() {
    // el navcontroller maneja el back stack (la pila de pantallas)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "popular") {

        // pantalla de lista, al tocar una pelicula navega al detalle pasando el id en la ruta
        composable("popular") {
            PopularScreen(onMovieClick = { id -> navController.navigate("detail/$id") })
        }

        // pantalla de detalle, {movieId} es el argumento que lee el detailviewmodel
        composable(
            route = "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            DetailScreen()
        }
    }
}