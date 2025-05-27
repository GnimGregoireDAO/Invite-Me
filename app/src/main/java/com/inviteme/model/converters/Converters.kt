package com.inviteme.model.converters

import androidx.room.TypeConverter
import java.sql.Timestamp

/**
 * Type Converters pour Room Database car il ne peut pas gérer
 * Timestamp directement.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}
