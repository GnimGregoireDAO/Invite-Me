package com.inviteme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inviteme.model.converters.Converters
import com.inviteme.model.dao.EvenementDAO
import com.inviteme.model.dao.LieuDao
import com.inviteme.model.dao.UtilisateurDAO
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu
import com.inviteme.model.entities.Utilisateur

@Database(entities = [Evenement::class, Lieu::class, Utilisateur::class], version = 6, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun evenementDAO(): EvenementDAO
    abstract fun lieuDao(): LieuDao
    abstract fun utilisateurDao(): UtilisateurDAO

    companion object {
        @Volatile // pour que cet objet reste un singleton malgré les side thread
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "invite_me_db"
                )
                    .fallbackToDestructiveMigration() // Will recreate the database if version changes
                    .build().also { INSTANCE = it }
            }
        }
    }
}