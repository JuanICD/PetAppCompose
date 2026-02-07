package com.example.petapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

// IMPORTS DE TUS PANTALLAS
import com.example.petapp.ui.screen.home.PetListScreen
import com.example.petapp.ui.screen.about.AboutScreen
import com.example.petapp.ui.screen.detail.DetailScreen
import com.example.petapp.ui.screen.start.StartScreen

/**
 * Rutas de navegación de la aplicación.
 */
sealed class Routes(val route: String) {
    object Start : Routes("start")
    object PetList : Routes("pet_list")
    object About : Routes("about")
    object PetDetail : Routes("pet_detail/{petName}") {
        fun createRoute(petName: String) = "pet_detail/$petName"
    }
}

/**
 * Composable principal que gestiona la navegación de la aplicación utilizando NavHost.
 * Se ha eliminado el Navigation 3 y la barra de navegación inferior por requerimiento.
 */
@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Start.route
    ) {
        // Pantalla de Inicio con dos botones
        composable(Routes.Start.route) {
            StartScreen(
                onNavigateToPets = { navController.navigate(Routes.PetList.route) },
                onNavigateToAbout = { navController.navigate(Routes.About.route) }
            )
        }

        // Pantalla de Lista de Mascotas (antes PetListScreen)
        composable(Routes.PetList.route) {
            PetListScreen(
                onPetClick = { name ->
                    navController.navigate(Routes.PetDetail.createRoute(name))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla About
        composable(Routes.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Detalle de Mascota
        composable(
            route = Routes.PetDetail.route,
            arguments = listOf(navArgument("petName") { type = NavType.StringType })
        ) { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName") ?: ""
            DetailScreen(
                petName = petName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
