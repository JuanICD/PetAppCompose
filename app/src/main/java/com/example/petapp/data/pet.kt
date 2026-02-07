package com.example.petapp.data

/**
 * Clase de datos que representa una mascota.
 * 
 * @property name Nombre de la mascota.
 * @property age Edad de la mascota.
 * @property race Raza de la mascota.
 * @property likes Cantidad de likes recibidos. Se usa para determinar si es favorita.
 * @property location Ciudad donde se encuentra la mascota.
 * @property imageRes ID del recurso de imagen local.
 */
data class Pet(
    val name: String,
    val age: Int,
    val race: String,
    var likes: Int,
    val location: String,
    val imageRes: Int
)

/**
 * Repositorio de mascotas que gestiona la lista de animales disponibles.
 * Se utiliza un objeto (Singleton) para mantener la persistencia de los datos en memoria
 * durante la sesión de la aplicación.
 */
object PetRepository {
    // Usamos una lista mutable para poder eliminar mascotas (adopción)
    private val _pets = mutableListOf(
        Pet("Luna", 2, "Labrador", 0, "Madrid", com.example.petapp.R.drawable.dog),
        Pet("Thor", 4, "Husky", 0, "Barcelona", com.example.petapp.R.drawable.dog),
        Pet("Milo", 1, "Beagle", 0, "Valencia", com.example.petapp.R.drawable.dog),
        Pet("Bella", 3, "Poodle", 0, "Sevilla", com.example.petapp.R.drawable.dog),
        Pet("Simba", 5, "Golden Retriever", 0, "Zaragoza", com.example.petapp.R.drawable.dog),
        Pet("Kira", 2, "German Shepherd", 0, "Malaga", com.example.petapp.R.drawable.dog),
        Pet("Rocky", 6, "Bulldog", 0, "Murcia", com.example.petapp.R.drawable.dog),
        Pet("Nala", 1, "Siamese Cat", 0, "Palma", com.example.petapp.R.drawable.cat),
        Pet("Coco", 3, "Persian Cat", 0, "Las Palmas", com.example.petapp.R.drawable.cat),
        Pet("Leo", 4, "Maine Coon", 0, "Bilbao", com.example.petapp.R.drawable.cat)
    )

    /**
     * Lista pública de mascotas (solo lectura para el exterior).
     */
    val pets: List<Pet> get() = _pets

    /**
     * Busca una mascota por su nombre.
     * 
     * @param name El nombre de la mascota a buscar.
     * @return La mascota encontrada o null si no existe.
     */
    fun getPetByName(name: String): Pet? {
        return _pets.find { it.name == name }
    }

    /**
     * Obtiene la lista de mascotas que tienen al menos un like.
     * 
     * @return Lista de mascotas favoritas.
     */
    fun getFavoritePets(): List<Pet> {
        return _pets.filter { it.likes > 0 }
    }

    /**
     * Elimina una mascota de la lista (proceso de adopción).
     * 
     * @param name Nombre de la mascota a eliminar.
     */
    fun removePet(name: String) {
        _pets.removeIf { it.name == name }
    }
}
