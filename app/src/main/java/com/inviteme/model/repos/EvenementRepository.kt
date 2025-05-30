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
    
    // Récupérer un événement par son ID
    suspend fun getEvenementById(id: Int): Evenement? {
        return evenementDao.getById(id)
    }
    
    // Rechercher des événements par titre
    suspend fun rechercherEvenements(titre: String): List<Evenement> {
        return evenementDao.getByTitre(titre)
    }
    
    // Supprimer un événement par son ID
    suspend fun supprimerEvenement(id: Int): Int {
        return evenementDao.deleteById(id)
    }
    
    // Supprimer un événement par l'objet
    suspend fun supprimerEvenement(evenement: Evenement) {
        evenementDao.delete(evenement)
    }
    
    // Mettre à jour un événement
    suspend fun updateEvenement(evenement: Evenement) {
        evenementDao.update(evenement)
    }

    suspend fun deleteAllEvenements() {
        evenementDao.deleteAll()
    }
}