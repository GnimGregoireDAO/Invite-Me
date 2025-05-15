package com.inviteme.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp


// cette classe n'est juste pas finie encore, juste fait pour des tests
@Entity(tableName = "evenements")
data class Evenement(
    @PrimaryKey(autoGenerate=true)
    val id: Int,
    val titre: String,
    val description: String?,
    val dateAjout: Timestamp,
    val dateModification: Timestamp
)