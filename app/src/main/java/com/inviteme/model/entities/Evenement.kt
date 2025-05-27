package com.inviteme.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(
    tableName = "evenements",
    foreignKeys = [
        ForeignKey(
            entity = Lieu::class,
            parentColumns = ["id"],
            childColumns = ["lieuId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("lieuId")]
)
data class Evenement(
    @PrimaryKey(autoGenerate=true)
    val id: Int = 0,
    val titre: String,
    val description: String?,
    val dateAjout: Timestamp,
    val dateModification: Timestamp,
    val lieuId: Int? = null
)
