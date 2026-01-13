package com.example.petapp

import com.example.petapp.data.repository.PetRepositoryImpl
import com.example.petapp.domain.repository.PetRepository
import com.example.petapp.domain.usecase.AddMascotaUseCase
import com.example.petapp.domain.usecase.GetPetsUseCase

object AppContainer {
    private val petRepository: PetRepository by lazy { PetRepositoryImpl() }

    val getPetsUseCase by lazy { GetPetsUseCase(petRepository) }
    val addPetUseCase by lazy { AddMascotaUseCase(petRepository) }
}