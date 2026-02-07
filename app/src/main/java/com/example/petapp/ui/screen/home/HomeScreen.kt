package com.example.petapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petapp.ui.theme.PetAppTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.petapp.data.Pet

/**
 * Pantalla principal que muestra la lista de todas las mascotas disponibles.
 * Permite buscar mascotas por nombre, navegar a su detalle y adoptarlas.
 * 
 * @param onPetClick Función para manejar la navegación al detalle de una mascota.
 * @param viewModel ViewModel que suministra los datos y lógica de la pantalla.
 */
@Composable
fun HomeScreen(
    onPetClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    // Estado para el texto de búsqueda
    var searchText by remember { mutableStateOf("") }
    // Observamos la lista de mascotas desde el ViewModel
    val pets by viewModel.pets.collectAsState()

    // Refrescamos la lista al entrar a la pantalla para reflejar cambios (como likes)
    // Se usa LaunchedEffect para que solo se ejecute al componer la pantalla por primera vez
    LaunchedEffect(Unit) {
        viewModel.refreshPets()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Barra de búsqueda personalizada
        SimpleSearchBar(data = searchText, onValueChange = { searchText = it })
        
        // Lista vertical de mascotas filtrada por el texto de búsqueda
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pets.filter { it.name.contains(searchText, ignoreCase = true) }) { pet ->
                PetCard(
                    name = pet.name,
                    age = "${pet.age} años",
                    location = pet.location,
                    imageRes = pet.imageRes,
                    // Al pulsar en la tarjeta navegamos al detalle
                    onClick = { onPetClick(pet.name) },
                    // Al pulsar en adoptar eliminamos la mascota de la vista
                    onAdoptClick = { viewModel.adoptPet(pet.name) }
                )
            }
        }
    }
}

/**
 * Barra de búsqueda sencilla.
 * 
 * @param data Texto actual de búsqueda.
 * @param onValueChange Callback cuando el texto cambia.
 * @param modifier Modificador opcional para personalizar el componente.
 */
@Composable
fun SimpleSearchBar(
    data: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = data,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .shadow(4.dp, MaterialTheme.shapes.extraLarge),
        placeholder = {
            Text(text = "Buscar mascotas por nombre...", color = Color.Gray)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        singleLine = true
    )
}

/**
 * Tarjeta que muestra un resumen de la información de una mascota.
 * Rediseñada para ser clickable y navegar al detalle.
 * 
 * @param name Nombre de la mascota.
 * @param age Edad formateada.
 * @param location Ubicación de la mascota.
 * @param imageRes ID del recurso de imagen.
 * @param onClick Callback al pulsar la tarjeta para navegar.
 * @param onAdoptClick Callback al pulsar el botón de Adoptar.
 */
@Composable
fun PetCard(
    name: String,
    age: String,
    location: String,
    imageRes: Int,
    onClick: () -> Unit,
    onAdoptClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }, // Hacemos toda la tarjeta clickable para navegar
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Contenedor de la imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = imageRes,
                    contentDescription = "Foto de $name",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Contenedor de información y acciones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$name, $age",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    // Botón de Adoptar (ahora elimina de la vista)
                    Button(
                        onClick = onAdoptClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape
                    ) {
                        Text(text = "Adoptar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetCardPreview() {
    PetAppTheme {
        HomeScreen(onPetClick = {})
    }
}
