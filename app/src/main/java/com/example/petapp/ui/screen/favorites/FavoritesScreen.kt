package com.example.petapp.ui.screen.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petapp.data.PetRepository
import com.example.petapp.ui.screen.home.PetCard

/**
 * Pantalla de Favoritos.
 * Muestra las mascotas que tienen el valor de likes superior a 0.
 * 
 * @param onPetClick Callback para navegar al detalle de la mascota seleccionada.
 * @param viewModel ViewModel que gestiona la lógica de favoritos.
 */
@Composable
fun FavoritesScreen(
    onPetClick: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {
    // Cada vez que entramos a la pantalla, refrescamos la lista de favoritos
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favorites by viewModel.favoritePets.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            // Estado vacío: No hay mascotas con likes
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes favoritos seleccionados.\n¡Dale a 'Adoptar' y pulsa el corazón!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            // Lista de mascotas que tienen likes
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) { pet ->
                    PetCard(
                        name = pet.name,
                        age = "${pet.age} años",
                        location = pet.location,
                        imageRes = pet.imageRes,
                        // Navegación al detalle
                        onClick = { onPetClick(pet.name) },
                        // En favoritos, el botón adoptar también elimina la mascota globalmente
                        onAdoptClick = { 
                            PetRepository.removePet(pet.name)
                            viewModel.loadFavorites() 
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FavoritesScreenPreview() = FavoritesScreen(onPetClick = {})