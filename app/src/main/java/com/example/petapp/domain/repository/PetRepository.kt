package com.example.petapp.domain.repository

import com.example.petapp.domain.model.Pet

interface PetRepository {

    fun getAllPets(): List<Pet>
    fun addPet(pet: Pet)
}