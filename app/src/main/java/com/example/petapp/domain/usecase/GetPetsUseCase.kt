package com.example.petapp.domain.usecase

import com.example.petapp.domain.repository.PetRepository

class GetPetsUseCase(
    private val repository: PetRepository
) {
    fun getPets() = repository.getAllPets()
}

