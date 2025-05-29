package com.inviteme.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "evenements_utilisateurs",
    primaryKeys = ["evenement_id", "utilisateur_id"],
    foreignKeys = [
        ForeignKey(
            entity = Evenement::class,
            parentColumns = ["id"],
            childColumns = ["evenement_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Utilisateur::class,
            parentColumns = ["id"],
            childColumns = ["utilisateur_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(" lieuId")]
)
data class EvenementUtilisateur(

    @ColumnInfo(index = true, name = "utilisateur_id")
    val utilisateurId: Int,

    @ColumnInfo(index = true, name = "evenement_id")
    val evnementId: Int,

    val role: String
    )