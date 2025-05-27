package com.example.petcare

import androidx.room.*

@Dao
interface NotificacionDao {

    @Insert
    suspend fun insertar(notificacion: Notificacion)

    @Query("SELECT * FROM notificaciones WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun obtenerPorUsuario(usuarioId: Int): List<Notificacion>

    @Query("DELETE FROM notificaciones WHERE id = :id")
    suspend fun eliminar(id: Int)

    @Query("UPDATE notificaciones SET completada = 1 WHERE id = :id")
    suspend fun marcarComoCompletada(id: Int)

    @Update
    suspend fun actualizar(notificacion: Notificacion)

}
