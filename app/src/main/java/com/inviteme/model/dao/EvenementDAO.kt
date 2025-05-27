package com.inviteme.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inviteme.model.entities.Evenement

@Dao
interface EvenementDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvenement(evenement: Evenement)

    @Query("SELECT * FROM evenements")
    fun getAll(): LiveData<List<Evenement>>
    
    // Méthode pour lister un événement par son ID
    @Query("SELECT * FROM evenements WHERE id = :id")
    suspend fun getById(id: Int): Evenement?
    
    // Méthode pour lister les événements par titre (recherche partielle)
    @Query("SELECT * FROM evenements WHERE titre LIKE '%' || :titre || '%'")
    suspend fun getByTitre(titre: String): List<Evenement>
    
    // Méthode pour supprimer un événement par son ID
    @Query("DELETE FROM evenements WHERE id = :id")
    suspend fun deleteById(id: Int): Int
    
    // Méthode alternative pour supprimer un événement en utilisant l'objet entier
    @Delete
    suspend fun delete(evenement: Evenement)
}