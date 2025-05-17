package com.inviteme.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inviteme.model.entities.Evenement

@Dao
interface EvenementDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvenement(evenement: Evenement)

    @Query("select * from invite_me_db.evenements")
    fun getAll(): LiveData<List<Evenement>>

}