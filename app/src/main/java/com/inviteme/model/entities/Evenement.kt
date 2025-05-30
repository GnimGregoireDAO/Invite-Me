package com.inviteme.model.entities

import androidx.room.ColumnInfo
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
            parentColumns = ["id"], // champs dans la table d'ou la clef étrangère correspond
            childColumns = ["lieu_id"], // champs dans la table actuelle ou la clef étrangère seras recopiée: on "recopie" les clef étrangères de la table parent vers la table enfant toujours
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["lieu_id"])] // permet de spéicifier un index, une clef pout accélérer la recherche.
)
data class Evenement(
    @PrimaryKey(autoGenerate=true)
    val id: Int = 0,
    val titre: String,
    val description: String?,
    val dateAjout: Timestamp,
    val dateModification: Timestamp,
    @ColumnInfo(name = "lieu_id")
    val lieuId: Long?
)
