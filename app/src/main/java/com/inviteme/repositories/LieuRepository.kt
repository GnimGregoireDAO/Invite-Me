package com.inviteme.repositories

import Lieu
import com.inviteme.dao.LieuDao

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
}