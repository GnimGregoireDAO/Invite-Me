package com.inviteme.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.inviteme.model.entities.Lieu

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