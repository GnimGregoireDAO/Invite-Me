package com.inviteme.model.repos

import androidx.lifecycle.LiveData
import com.inviteme.model.dao.UtilisateurDAO
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Utilisateur

class UtilisateurRepository(private val utilisateurDAO: UtilisateurDAO) {
    suspend fun ajouterEvenement(event: Utilisateur){
        utilisateurDAO.ajouterUtilisateur(event)
    }

    fun getAll(): LiveData<List<Utilisateur>> {
        return utilisateurDAO.getAll()
    }

    // Récupérer un événement par son ID
    suspend fun getEvenementById(id: Int): Utilisateur? {
        return utilisateurDAO.getById(id)
    }


    // Supprimer un événement par son ID
    suspend fun supprimerEvenement(id: Int): Int {
        return utilisateurDAO.deleteById(id)
    }

    // Supprimer un événement par l'objet
    suspend fun supprimerEvenement(evenement: Utilisateur) {
        utilisateurDAO.delete(evenement)
    }
}