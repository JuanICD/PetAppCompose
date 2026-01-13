package com.example.petapp.domain.usecase

import com.example.petapp.domain.model.Pet
import com.example.petapp.domain.repository.PetRepository

class AddMascotaUseCase(
    private val repository: PetRepository
) {
    fun addPet(pet: Pet) {
        if (pet.name.isEmpty()) throw IllegalArgumentException()
        repository.addPet(pet)
    }
}