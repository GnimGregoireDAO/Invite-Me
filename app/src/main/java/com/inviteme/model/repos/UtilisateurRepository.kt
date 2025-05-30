package com.inviteme.model.repos

import androidx.lifecycle.LiveData
import com.inviteme.model.dao.UtilisateurDAO
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Utilisateur

class UtilisateurRepository(private val utilisateurDAO: UtilisateurDAO) {
    suspend fun ajouterUtilisateur(event: Utilisateur){
        utilisateurDAO.ajouterUtilisateur(event)
    }

    fun getAll(): LiveData<List<Utilisateur>> {
        return utilisateurDAO.getAll()
    }

    // Récupérer un utilisateur par son ID
    suspend fun getEvenementById(id: Int): Utilisateur? {
        return utilisateurDAO.getById(id)
    }

    // Récupérer un utilisateur par son Email et password
    suspend fun getParEmailEtNom(email: String, nom: String): Utilisateur? {
        return utilisateurDAO.getByEmailAndPassword(email, nom)
    }



    // Supprimer un utilisateur par son ID
    suspend fun supprimerEvenement(id: Int): Int {
        return utilisateurDAO.deleteById(id)
    }

    // Supprimer un utilisateur par l'objet
    suspend fun supprimerEvenement(evenement: Utilisateur) {
        utilisateurDAO.delete(evenement)
    }
}