package com.example.petcare
import androidx.room.*

@Dao
interface MascotaDao {
    @Insert
    suspend fun insertar(mascota: Mascota): Long

    @Query("SELECT * FROM mascotas")
    suspend fun obtenerTodas(): List<Mascota>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Mascota?

    @Update
    suspend fun actualizar(mascota: Mascota)

    @Delete
    suspend fun eliminar(mascota: Mascota)

    @Query("SELECT * FROM mascotas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Mascota?

    // ✅ FILTRAR MASCOTAS POR USUARIO
    @Query("SELECT * FROM mascotas WHERE usuarioId = :usuarioId")
    suspend fun obtenerPorUsuario(usuarioId: Int): List<Mascota>
}
