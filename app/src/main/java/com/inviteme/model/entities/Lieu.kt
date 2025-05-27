package com.inviteme.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lieux")
data class Lieu(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "adresse")
    val adresse: String,

    @ColumnInfo(name = "pays")
    val pays: String

    /*@ColumnInfo(name = "ville")
    val ville: String = "",

    @ColumnInfo(name = "code_postal")
    val codePostal: String = "",

    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,

    @ColumnInfo(name = "longitude")
    val longitude: Double? = null*/
)