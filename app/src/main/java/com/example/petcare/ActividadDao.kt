package com.example.petcare

import androidx.room.*

@Dao
interface ActividadDao {

    @Insert
    suspend fun insertar(actividad: Actividad)

    @Update
    suspend fun actualizar(actividad: Actividad)

    @Delete
    suspend fun eliminar(actividad: Actividad)

    @Query("SELECT * FROM actividades WHERE mascotaId = :mascotaId ORDER BY fecha DESC")
    suspend fun obtenerPorMascota(mascotaId: Int): List<Actividad>

    @Query("SELECT * FROM actividades WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Actividad?

    @Query("SELECT * FROM actividades WHERE fecha = :fecha")
    suspend fun obtenerPorFecha(fecha: String): List<Actividad>


}
