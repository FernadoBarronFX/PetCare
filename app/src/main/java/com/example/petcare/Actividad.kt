package com.example.petcare

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class Actividad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String // Se puede usar formato "dd/MM/yyyy"
)
