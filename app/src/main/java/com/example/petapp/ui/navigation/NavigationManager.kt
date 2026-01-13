package com.example.petapp.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object Home : NavKey

@Serializable
data object AddPet : NavKey

@Serializable
data class PetDetail(val petId: String) : NavKey