package com.example.petcare

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascotas")
data class Mascota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val edad: String,
    val especie: String,
    val sexo: String,
    val raza: String,
    val color: String,
    val peso: String,  // 👈 NUEVO CAMPO
    val enfermedades: String,
    val imagenUri: String, // para guardar la URI de la imagen desde el almacenamiento
    val usuarioId: Int // clave foránea para enlazar con el usuario dueño
)
