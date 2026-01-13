package com.example.petapp.domain.model

data class Pet(
    val id: Int,
    val name: String,
    val type: String?,
    val age: Int?,
    val descriptor: String,
    val image: Int
)