package com.example.petapp.ui.navigation

// IMPORTS DE ANDROID Y COMPOSE
import android.R
import android.view.Surface
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
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

// IMPORTS DE TUS PANTALLAS (Asegúrate de que estas rutas sean correctas en tu proyecto)
import com.example.petapp.ui.screen.home.HomeScreen
import com.example.petapp.ui.screen.add.AddPetScreen
import com.example.petapp.ui.screen.about.AboutScreen
import java.nio.file.WatchEvent

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
data class PetDetail(val petId: Int) : NavKey

// --- 2. COMPOSABLE PRINCIPAL (NavManager) ---
@Composable
fun MainScaffold() {
    // Lógica de stacks y pestañas (igual que antes) [cite: 509, 538]
    val homeStack = rememberNavBackStack(Home)
    val addPetStack = rememberNavBackStack(AddPet)
    val aboutStack = rememberNavBackStack(About)
    var currentTab by remember { mutableStateOf<NavKey>(Home) }

    val currentStack = when (currentTab) {
        Home -> homeStack
        AddPet -> addPetStack
        About -> aboutStack
        else -> homeStack
    }

    Scaffold(
        bottomBar = {
            // Usamos nuestro navbar personalizado aquí
            CustomBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavDisplay(
                backStack = currentStack,
                onBack = { /* lógica */ },
                entryProvider = entryProvider {
                    entry<Home> {
                        HomeScreen(onPetClick ={} )
                    }
                    entry<AddPet> {
                        AddPetScreen(onSaved = {})
                    }
                    entry<About> {
                        AboutScreen()
                    }

                }
            )
        }
    }
}

@Composable
fun CustomBottomNavBar(
    currentTab: NavKey,
    onTabSelected: (NavKey) -> Unit
) {

    //Definir pestañas
    val items = listOf(Home, AddPet, About)

    //Contenedor principal
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
                    onClick = { onTabSelected(item) }
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
        AddPet -> Icons.Default.Add
        About -> Icons.Default.Info
        else -> Icons.Default.Home
    }
}
fun getLabelFor(item:NavKey): String{
    return when(item){
        Home -> "Home"
        AddPet -> "Add"
        About -> "Info"
        else -> "Inicio"
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewCustomBottomNavBar() {
    CustomBottomNavBar(
        currentTab = Home,
        onTabSelected = {}
    )
}
