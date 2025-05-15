package com.inviteme.model.repos

import androidx.lifecycle.LiveData
import com.inviteme.model.dao.EvenementDAO
import com.inviteme.model.entities.Evenement

class EvenementRepository(private val evenementDao: EvenementDAO) {
    suspend fun ajouterEvenement(event: Evenement){
        evenementDao.addEvenement(event)
    }

    fun getAll(): LiveData<List<Evenement>>{
        return evenementDao.getAll()
    }
}