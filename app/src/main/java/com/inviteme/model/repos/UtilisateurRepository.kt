package com.inviteme.model.repos

import androidx.lifecycle.LiveData
import com.inviteme.model.dao.UtilisateurDAO
import com.inviteme.model.entities.Utilisateur

class UtilisateurRepository(private val utilisateurDAO: UtilisateurDAO) {

    // Ajouter un utilisateur
    suspend fun ajouterUtilisateur(utilisateur: Utilisateur) {
        utilisateurDAO.ajouterUtilisateur(utilisateur)
    }

    // Obtenir la liste de tous les utilisateurs (observable)
    fun getAll(): LiveData<List<Utilisateur>> {
        return utilisateurDAO.getAll()
    }

    // Récupérer un utilisateur par son ID
    suspend fun getUtilisateurById(id: Int): Utilisateur? {
        return utilisateurDAO.getById(id)
    }

    // Récupérer un utilisateur par son email et mot de passe
    suspend fun getParEmailEtMotDePasse(email: String, motDePasse: String): Utilisateur? {
        return utilisateurDAO.getByEmailAndPassword(email, motDePasse)
    }

    // Supprimer un utilisateur par son ID
    suspend fun supprimerUtilisateurParId(id: Int): Int {
        return utilisateurDAO.deleteById(id)
    }

    // Supprimer un utilisateur par objet
    suspend fun supprimerUtilisateur(utilisateur: Utilisateur) {
        utilisateurDAO.delete(utilisateur)
    }

    suspend fun countByEmail(email: String): Int {
        return utilisateurDAO.countByEmail(email)
    }

    suspend fun getParEmail(email: String): Utilisateur? {
        return utilisateurDAO.getByEmail(email)
    }
}
