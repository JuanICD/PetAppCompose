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
}

