package com.example.petapp.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.petapp.data.Pet
import com.example.petapp.data.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para la pantalla Home.
 * Se encarga de cargar y gestionar la lista de mascotas para la pantalla principal,
 * incluyendo la funcionalidad de adopción.
 */
class PetListViewModel : ViewModel() {
    // Estado interno que contiene la lista de mascotas actual
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    // Estado público observable desde la UI
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    init {
        // Al instanciar el ViewModel, cargamos las mascotas iniciales
        loadPets()
    }

    /**
     * Carga la lista inicial de mascotas desde el repositorio.
     */
    private fun loadPets() {
        // Asignamos la lista del repositorio al estado
        _pets.value = PetRepository.pets.toList()
    }

    /**
     * Refresca la lista de mascotas desde el repositorio.
     * Útil para cuando volvemos de la pantalla de detalle y queremos asegurar
     * que el estado de la lista está actualizado.
     */
    fun refreshPets() {
        // Forzamos una nueva emisión creando una copia de la lista para que StateFlow detecte el cambio
        _pets.value = PetRepository.pets.toList()
    }

    /**
     * Gestiona la adopción de una mascota eliminándola del repositorio y actualizando la vista.
     * 
     * @param name Nombre de la mascota a adoptar.
     */
    fun adoptPet(name: String) {
        // Eliminamos del repositorio central
        PetRepository.removePet(name)
        // Actualizamos el estado local de la pantalla Home para que desaparezca de la lista
        refreshPets()
    }
}
