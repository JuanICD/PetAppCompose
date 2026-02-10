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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petapp.ui.theme.PetAppTheme

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

/**
 * Pantalla principal que muestra la lista de todas las mascotas disponibles.
 * Permite buscar mascotas por nombre, navegar a su detalle y adoptarlas.
 * 
 * @param onPetClick Función para manejar la navegación al detalle de una mascota.
 * @param onBack Función para volver a la pantalla anterior.
 * @param viewModel ViewModel que suministra los datos y lógica de la pantalla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    onPetClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: PetListViewModel = viewModel()
) {
    // Observamos la lista de mascotas desde el ViewModel
    val pets by viewModel.pets.collectAsState()

    // Refrescamos la lista al entrar a la pantalla para reflejar cambios
    LaunchedEffect(Unit) {
        viewModel.refreshPets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nuestras Mascotas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Lista vertical de mascotas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pets) { pet ->
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
        onClick = onClick, // Usamos el parámetro onClick nativo de Card
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
        PetListScreen(onPetClick = {}, onBack = {})
    }
}
