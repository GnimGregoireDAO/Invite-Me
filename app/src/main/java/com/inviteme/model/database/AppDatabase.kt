package com.inviteme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inviteme.model.dao.EvenementDAO
import com.inviteme.model.entities.Evenement

@Database(entities = [Evenement::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun evenementDAO(): EvenementDAO

    companion object {
        @Volatile // pour que cet objet reste un singleton malgré les side thread
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "invite_me_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
        }
    }
}