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
        Pet("Luna", 2, "Labrador", "Madrid", com.example.petapp.R.drawable.dog),
        Pet("Thor", 4, "Husky", "Barcelona", com.example.petapp.R.drawable.dog),
        Pet("Milo", 1, "Beagle", "Valencia", com.example.petapp.R.drawable.dog),
        Pet("Bella", 3, "Poodle", "Sevilla", com.example.petapp.R.drawable.dog),
        Pet("Simba", 5, "Golden Retriever", "Zaragoza", com.example.petapp.R.drawable.dog),
        Pet("Kira", 2, "German Shepherd", "Malaga", com.example.petapp.R.drawable.dog),
        Pet("Rocky", 6, "Bulldog", "Murcia", com.example.petapp.R.drawable.dog),
        Pet("Nala", 1, "Siamese Cat", "Palma", com.example.petapp.R.drawable.cat),
        Pet("Coco", 3, "Persian Cat", "Las Palmas", com.example.petapp.R.drawable.cat),
        Pet("Leo", 4, "Maine Coon", "Bilbao", com.example.petapp.R.drawable.cat)
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
     * Elimina una mascota de la lista (proceso de adopción).
     * 
     * @param name Nombre de la mascota a eliminar.
     */
    fun removePet(name: String) {
        _pets.removeIf { it.name == name }
    }
}
