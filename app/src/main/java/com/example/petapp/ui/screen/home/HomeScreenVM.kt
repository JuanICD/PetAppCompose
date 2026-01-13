package com.example.petapp.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.petapp.domain.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.petapp.domain.usecase.*

class HomeScreenVM(
    private val getPetsUseCase: GetPetsUseCase
) : ViewModel() {
    //Recuperar estado de la UI
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()


    init {
        loadPets()
    }

    private fun loadPets() {
        _pets.value = getPetsUseCase.getPets()
    }

}