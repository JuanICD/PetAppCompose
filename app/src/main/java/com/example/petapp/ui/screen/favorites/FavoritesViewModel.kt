package com.example.petapp.ui.screen.favorites

import androidx.lifecycle.ViewModel
import com.example.petapp.data.Pet
import com.example.petapp.data.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la pantalla de Favoritos.
 * Gestiona la lista de mascotas que han recibido al menos un "like".
 */
class FavoritesViewModel : ViewModel() {
    private val _favoritePets = MutableStateFlow<List<Pet>>(emptyList())
    val favoritePets: StateFlow<List<Pet>> = _favoritePets.asStateFlow()

    /**
     * Carga las mascotas favoritas desde el repositorio.
     * Se llama cada vez que la pantalla de favoritos se hace visible para asegurar que los datos están actualizados.
     */
    fun loadFavorites() {
        // Obtenemos del repositorio solo aquellas mascotas con likes > 0
        _favoritePets.value = PetRepository.getFavoritePets()
    }
}
