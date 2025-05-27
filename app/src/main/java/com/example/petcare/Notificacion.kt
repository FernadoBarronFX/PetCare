package com.example.petcare

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notificaciones")
data class Notificacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val completada: Boolean = false
)