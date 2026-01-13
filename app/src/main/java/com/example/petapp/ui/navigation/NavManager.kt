package com.example.petapp.ui.navigation

// IMPORTS DE ANDROID Y COMPOSE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// IMPORTS DE NAVIGATION 3
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

// IMPORTS DE KOTLIN SERIALIZATION
import kotlinx.serialization.Serializable

// IMPORTS DE TUS PANTALLAS (Asegúrate de que estas rutas sean correctas en tu proyecto)
import com.example.petapp.ui.screen.home.HomeScreen
import com.example.petapp.ui.screen.add.AddPetScreen
import com.example.petapp.ui.screen.about.AboutScreen
// import com.example.petapp.ui.screen.detail.DetailScreen // Descomenta si ya tienes esta pantalla

// --- 1. DEFINICIÓN DE LAS LLAVES (RUTAS) ---
// Usamos @Serializable porque rememberNavBackStack lo requiere para funcionar correctamente[cite: 436, 438].
@Serializable
data object Home : NavKey

@Serializable
data object AddPet : NavKey

@Serializable
data object About : NavKey

@Serializable
data class PetDetail(val petId: String) : NavKey

// --- 2. COMPOSABLE PRINCIPAL (NavManager) ---
@Composable
fun MainScaffold() {
    // A. Definimos las rutas principales para el menú
    val topLevelRoutes = listOf(Home, AddPet, About)

    // B. Creamos un "Historial" (BackStack) independiente para cada pestaña[cite: 384, 565].
    // rememberNavBackStack maneja la persistencia de la lista automáticamente.
    val homeStack = rememberNavBackStack(Home)
    val addPetStack = rememberNavBackStack(AddPet)
    val aboutStack = rememberNavBackStack(About)

    // C. Mapeamos cada ruta a su historial correspondiente
    val stacks = mapOf(
        Home to homeStack,
        AddPet to addPetStack,
        About to aboutStack
    )

    // D. Estado de la pestaña actual
    // CAMBIO: Usamos 'remember' simple en lugar de 'rememberSerializable'.
    // Esto evita el crash, pero si rotas la pantalla, volverá a "Home".
    var currentTab by remember { mutableStateOf<NavKey>(Home) }

    // E. ESTRUCTURA VISUAL (Barra de navegación + Contenido)
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            // Generamos los botones del menú
            topLevelRoutes.forEach { route ->
                item(
                    icon = {
                        // Iconos según la ruta
                        val icon = when (route) {
                            Home -> Icons.Default.Home
                            About -> Icons.Default.Info
                            else -> Icons.Default.Settings
                        }
                        Icon(icon, contentDescription = route.toString())
                    },
                    label = { Text(route.toString()) },
                    selected = route == currentTab,
                    onClick = { currentTab = route } // Cambiar de pestaña al hacer click
                )
            }
        }
    ) {
        // F. EL MOTOR DE NAVEGACIÓN (NavDisplay) [cite: 568, 889]
        // Muestra el stack correspondiente a la pestaña que el usuario seleccionó
        val currentStack = stacks[currentTab]!!

        NavDisplay(
            backStack = currentStack,
            onBack = { currentStack.removeLastOrNull() }, // Lógica simple de botón atrás [cite: 868]
            entryProvider = entryProvider {
                // G. VINCULACIÓN: Llave -> Pantalla

                entry<Home> {
                    HomeScreen(
                        // Ejemplo: Al hacer click en una mascota, navegamos al detalle en el stack de Home
                        onPetClick = {/* TODO petId -> homeStack.add(PetDetail(petId)) */}
                    )
                }

                entry<AddPet> {
                    AddPetScreen(
                        // Ejemplo: Al guardar, podríamos querer volver a Home o limpiar el stack
                        onSaved = { /* Tu lógica aquí */ }
                    )
                }

                entry<About> {
                    AboutScreen()
                }

                // Ejemplo de ruta con argumentos
                entry<PetDetail> { key ->
                    // DetailScreen(petId = key.petId) // Asegúrate de tener este Composable creado
                    Text("Detalle de mascota: ${key.petId}") // Placeholder por si no tienes la pantalla
                }
            }
        )
    }
}