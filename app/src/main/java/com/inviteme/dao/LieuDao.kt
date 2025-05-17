package com.inviteme.dao
import Lieu
import androidx.room.*


@Dao
interface LieuDao {
    @Query("SELECT * FROM lieux")
    suspend fun getAll(): List<Lieu>

    @Query("SELECT * FROM lieux WHERE id = :id")
    suspend fun getById(id: Int): Lieu?

    @Insert
    suspend fun insert(lieu: Lieu): Long

    @Update
    suspend fun update(lieu: Lieu)

    @Delete
    suspend fun delete(lieu: Lieu)
}