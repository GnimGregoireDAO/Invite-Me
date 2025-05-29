package com.inviteme.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "Utilisateurs")
data class Utilisateur(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "nom")
    val nom: String,

    @ColumnInfo(name = "prenom")
    val prenom: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "dateAjout")
    val dateAjout: Timestamp,

    @ColumnInfo(name = "dateModification")
    val dateModification: Timestamp

)