package com.example.petapp.ui.screen.detail
import androidx.lifecycle.ViewModel
import com.example.petapp.data.Pet
import com.example.petapp.data.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel encargado de gestionar la lógica de la pantalla de detalles de una mascota.
 */
class DetailScreenVM : ViewModel() {
    // Estado privado que contiene la mascota seleccionada (puede ser null inicialmente)
    private val _pet = MutableStateFlow<Pet?>(null)
    // Estado público para observar los cambios en la mascota desde la UI
    val pet: StateFlow<Pet?> = _pet.asStateFlow()

    /**
     * Carga los datos de una mascota específica por su nombre desde el repositorio.
     * Se realiza al iniciar la pantalla de detalle.
     * 
     * @param name Nombre de la mascota a buscar en el repositorio.
     */
    fun loadPet(name: String) {
        _pet.value = PetRepository.getPetByName(name)
    }

    /**
     * Incrementa el número de likes de la mascota y actualiza el estado.
     * Al tener al menos un like, la mascota aparecerá automáticamente en la lista de favoritos.
     */
    fun addLike() {
        _pet.value?.let { currentPet ->
            // Incrementamos los likes directamente en el objeto del repositorio.
            // Esto permite que el cambio sea persistente durante la sesión.
            currentPet.likes++
            
            // Forzamos la actualización del StateFlow emitiendo una nueva instancia (copia).
            // Compose requiere una nueva referencia para detectar el cambio de estado y recomponer.
            _pet.value = currentPet.copy(likes = currentPet.likes)
        }
    }
}

