package com.inviteme.model.repos

import com.inviteme.model.dao.LieuDao
import com.inviteme.model.entities.Lieu

class LieuRepository(private val lieuDao: LieuDao) {

    suspend fun getAllLieux(): List<Lieu> {
        return lieuDao.getAll()
    }

    suspend fun getLieuById(id: Int): Lieu? {
        return lieuDao.getById(id)
    }

    suspend fun saveLieu(lieu: Lieu): Long {
        return if (lieu.id == 0) {
            lieuDao.insert(lieu)
        } else {
            lieuDao.update(lieu)
            lieu.id.toLong()
        }
    }

    suspend fun deleteLieu(lieu: Lieu) {
        lieuDao.delete(lieu)
    }

    suspend fun deleteAllLieux() {
        lieuDao.deleteAll()
    }
}