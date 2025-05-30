package com.inviteme.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Utilisateur

@Dao
interface UtilisateurDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUtilisateur(utilisateur: Utilisateur)

    @Query("SELECT * FROM utilisateurs")
    fun getAll(): LiveData<List<Utilisateur>>

    // Méthode pour lister un utilisateur par son ID
    @Query("SELECT * FROM utilisateurs WHERE id = :id")
    suspend fun getById(id: Int): Utilisateur?

    // Méthode pour lister un utilisateur par son email
    @Query("SELECT * FROM utilisateurs WHERE email = :email")
    suspend fun getByEmail(email: String): Utilisateur?

    // Méthode user par email et password
    @Query("SELECT * FROM utilisateurs WHERE email = :email AND password = :password")
    suspend fun getByEmailAndPassword(email: String, password: String): Utilisateur?



    // Méthode pour supprimer un utilisateur par son ID
    @Query("DELETE FROM utilisateurs WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    // Méthode ajouter un utilisateur
    @Insert
    suspend fun ajouterUtilisateur(utilisateur: Utilisateur)

    // Méthode alternative pour supprimer un utilisateur en utilisant l'objet entier
    @Delete
    suspend fun delete(utilisateur: Utilisateur)

    @Query("SELECT COUNT(*) FROM utilisateurs WHERE email = :email")
    suspend fun countByEmail(email: String): Int




}