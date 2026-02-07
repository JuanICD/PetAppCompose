package com.example.petapp.ui.navigation

// IMPORTS DE ANDROID Y COMPOSE
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// IMPORTS DE NAVIGATION 3
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

// IMPORTS DE KOTLIN SERIALIZATION
import kotlinx.serialization.Serializable

// IMPORTS DE TUS PANTALLAS
import com.example.petapp.ui.screen.home.HomeScreen
import com.example.petapp.ui.screen.favorites.FavoritesScreen
import com.example.petapp.ui.screen.about.AboutScreen
import com.example.petapp.ui.screen.detail.DetailScreen

// --- 1. DEFINICIÓN DE LAS LLAVES (RUTAS) ---
@Serializable
data object Home : NavKey

@Serializable
data object Favorites : NavKey

@Serializable
data object About : NavKey

@Serializable
data class PetDetail(val petName: String) : NavKey

/**
 * Composable principal que gestiona la navegación de la aplicación utilizando Navigation 3.
 * Controla tres stacks independientes para Home, Favoritos e Info.
 */
@Composable
fun MainScaffold() {
    // Definimos los stacks de navegación como estados mutables de listas de llaves (NavKey)
    // Esto permite que al añadir o quitar llaves, la UI se actualice automáticamente.
    var homeKeys by remember { mutableStateOf(listOf<NavKey>(Home)) }
    var favoritesKeys by remember { mutableStateOf(listOf<NavKey>(Favorites)) }
    var aboutKeys by remember { mutableStateOf(listOf<NavKey>(About)) }

    // Creamos los objetos NavBackStack requeridos por NavDisplay para cada pestaña
    val homeStack = rememberNavBackStack(*homeKeys.toTypedArray())
    val favoritesStack = rememberNavBackStack(*favoritesKeys.toTypedArray())
    val aboutStack = rememberNavBackStack(*aboutKeys.toTypedArray())
    
    // Estado para controlar qué pestaña está activa actualmente
    var currentTab by remember { mutableStateOf<NavKey>(Home) }

    // Seleccionamos el stack y las llaves correspondientes a la pestaña activa
    val currentStack = when (currentTab) {
        Home -> homeStack
        Favorites -> favoritesStack
        About -> aboutStack
        else -> homeStack
    }

    val currentKeys = when (currentTab) {
        Home -> homeKeys
        Favorites -> favoritesKeys
        About -> aboutKeys
        else -> homeKeys
    }

    // Función auxiliar para actualizar las llaves del stack actual (navegar hacia adelante)
    val navigateTo: (NavKey) -> Unit = { newKey ->
        when (currentTab) {
            Home -> homeKeys = homeKeys + newKey
            Favorites -> favoritesKeys = favoritesKeys + newKey
            About -> aboutKeys = aboutKeys + newKey
        }
    }

    // Función para navegar hacia atrás eliminando la última llave del stack
    val navigateBack: () -> Unit = {
        when (currentTab) {
            Home -> if (homeKeys.size > 1) homeKeys = homeKeys.dropLast(1)
            Favorites -> if (favoritesKeys.size > 1) favoritesKeys = favoritesKeys.dropLast(1)
            About -> if (aboutKeys.size > 1) aboutKeys = aboutKeys.dropLast(1)
        }
    }

    Scaffold(
        bottomBar = {
            // Barra de navegación personalizada
            CustomBottomNavBar(
                currentTab = currentTab,
                onTabSelect = { 
                    // Si pulsamos la misma pestaña, volvemos a la raíz de ese stack
                    if (currentTab == it) {
                        when (it) {
                            Home -> homeKeys = listOf(Home)
                            Favorites -> favoritesKeys = listOf(Favorites)
                            About -> aboutKeys = listOf(About)
                        }
                    }
                    currentTab = it 
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // NavDisplay es el componente de Navigation 3 que renderiza la pantalla actual del stack
            NavDisplay(
                backStack = currentStack,
                onBack = navigateBack,
                entryProvider = entryProvider {
                    // Definición de destinos para el stack
                    entry<Home> {
                        HomeScreen(onPetClick = { name ->
                            // Al hacer clic en una mascota, añadimos PetDetail al stack de Home
                            navigateTo(PetDetail(name))
                        })
                    }
                    entry<Favorites> {
                        FavoritesScreen(onPetClick = { name ->
                             // También permitimos navegar al detalle desde favoritos
                             navigateTo(PetDetail(name))
                        })
                    }
                    entry<About> {
                        AboutScreen()
                    }
                    entry<PetDetail> { key ->
                        // Pantalla de detalle que recibe el nombre de la mascota
                        DetailScreen(
                            petName = key.petName,
                            onBack = navigateBack
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CustomBottomNavBar(
    currentTab: NavKey,
    onTabSelect: (NavKey) -> Unit
) {

    // Definimos las 3 pestañas principales: Home, Favoritos y About
    val items = listOf(Home, Favorites, About)

    // Contenedor principal de la barra de navegación
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 20.dp
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->

                NavItem(
                    item = item,
                    isSelected = currentTab == item,
                    onClick = { onTabSelect(item) }
                )

            }
        }

    }

}

@Composable
fun NavItem(
    item: NavKey,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Cambiamos el color y el tamaño basándonos en si está seleccionado
    val contentColor = if (isSelected) Color(0xFFE64A19) else Color.Gray
    val backgroundColor = if (isSelected) Color(0xFFFFE0B2) else Color.Transparent

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor) // Fondo solo si está seleccionado
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = getIconFor(item),
                contentDescription = null,
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = getLabelFor(item).uppercase(),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )

        }
    }

}

fun getIconFor(item: NavKey): ImageVector {
    return when (item) {
        Home -> Icons.Default.Home
        Favorites -> Icons.Default.Favorite
        About -> Icons.Default.Info
        else -> Icons.Default.Home
    }
}
fun getLabelFor(item:NavKey): String{
    return when(item){
        Home -> "Home"
        Favorites -> "Favoritos"
        About -> "Info"
        else -> "Inicio"
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewCustomBottomNavBar() {
    CustomBottomNavBar(
        currentTab = Home,
        onTabSelect = {}
    )
}
