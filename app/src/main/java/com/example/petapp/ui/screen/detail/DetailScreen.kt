package com.example.petapp.ui.screen.detail

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

/**
 * Pantalla de detalle de una mascota seleccionada.
 * Rediseñada para mostrar una imagen centrada estilo card y datos formateados debajo.
 *
 * @param petName El nombre de la mascota a mostrar (usado como identificador).
 * @param onBack Función callback para volver a la pantalla anterior.
 * @param viewModel Instancia del ViewModel para gestionar el estado de esta pantalla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    petName: String,
    onBack: () -> Unit,
    viewModel: DetailScreenVM = viewModel()
) {
    // Al iniciar la pantalla, cargamos los datos de la mascota usando su nombre
    LaunchedEffect(petName) {
        viewModel.loadPet(petName)
    }

    // Observamos el estado de la mascota desde el ViewModel
    val pet by viewModel.pet.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = pet?.name ?: "Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        //Solo dibuja el contenido si hay datos de la mascota si no, muestra un indicador de carga
        pet?.let { petData ->

            var scale by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Imagen de la mascota centrada estilo Card
                Card(
                    modifier = Modifier
                        .size(280.dp)
                        .shadow(12.dp, MaterialTheme.shapes.extraLarge)
                        .clipToBounds()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                //Actualizar el zoom para que no sea infinito
                                scale = (scale * zoom).coerceIn(0.9f, 1.1f)

                                //Solo permite mover la imagen si tiene zoom
                                if (scale > 1f) {
                                    offset += pan
                                }else{
                                    offset = Offset.Zero // Para resetear la posicion
                                }
                            }
                        },
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    AsyncImage(
                        model = petData.imageRes,
                        contentDescription = "Foto de ${petData.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Datos en texto formateado debajo de la imagen
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = petData.name,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${petData.race} • ${petData.age} años",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = petData.location,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Este simpático ${petData.race} llamado ${petData.name} está buscando una familia. " +
                                "Tiene ${petData.age} años y se encuentra en ${petData.location}. " +
                                "Es muy sociable y le encanta jugar.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón de Adoptar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* Acción de adopción */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Adoptar a ${petData.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}