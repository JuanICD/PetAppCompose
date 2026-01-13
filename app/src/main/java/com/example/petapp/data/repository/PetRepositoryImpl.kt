package com.example.petapp.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.petapp.R
import com.example.petapp.domain.model.Pet
import com.example.petapp.domain.repository.PetRepository

class PetRepositoryImpl : PetRepository {

    private val mockPets = mutableStateListOf<Pet>(
        Pet(1, "Max", "Perro", 2, "Muy bonito", R.drawable.dog),
        Pet(2,"Luna","Gato",3,"Cariñosa",R.drawable.cat)
    )

    override fun getAllPets(): List<Pet> {
        return mockPets
    }

    override fun addPet(pet: Pet) {
        mockPets.add(pet)
    }

}