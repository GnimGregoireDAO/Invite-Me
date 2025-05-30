package com.inviteme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Utilisateur
import com.inviteme.model.repos.UtilisateurRepository
import kotlinx.coroutines.launch
import java.sql.Timestamp

// ViewModel : permet de gérer les données entre l'UI (Activity) et la base de données
class InscriptionViewModel(application: Application) : AndroidViewModel(application) {

    // On crée un objet repository qui va permettre d'interagir avec la DAO
    private val repository: UtilisateurRepository

    // Bloc init : s'exécute automatiquement à la création du ViewModel
    init {
        // On récupère l'objet DAO depuis la base de données
        val utilisateurDao = AppDatabase.getDatabase(application).utilisateurDao()
        // On initialise le repository avec le DAO
        repository = UtilisateurRepository(utilisateurDao)
    }

    /**
     * Fonction pour inscrire un nouvel utilisateur
     *
     * @param prenom le prénom entré par l'utilisateur
     * @param email l'adresse email entrée
     * @param motDePasse le mot de passe choisi
     * @param onSuccess une fonction appelée si tout se passe bien
     * @param onError une fonction appelée s'il y a une erreur
     */
    fun inscrire(
        prenom: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // viewModelScope.launch lance un bloc de code en arrière-plan (coroutine)
        viewModelScope.launch {
            try {
                // Vérifie si un utilisateur existe déjà avec cet email et ce mot de passe
                val utilisateurExistant = repository.getParEmailEtNom(email, password)

                if (utilisateurExistant != null) {
                    // Si oui, on affiche une erreur
                    onError("Un utilisateur avec cet e-mail existe déjà.")
                } else {
                    // Sinon, on crée un nouvel utilisateur
                    val maintenant = Timestamp(System.currentTimeMillis()) // date actuelle

                    val nouvelUtilisateur = Utilisateur(
                        email = email,
                        prenom = prenom,
                        password = password,
                        dateAjout = maintenant,
                        dateModification = maintenant
                    )

                    // On ajoute l'utilisateur dans la base de données
                    repository.ajouterUtilisateur(nouvelUtilisateur)

                    // Appel de la fonction de succès (ex : redirection)
                    onSuccess()
                }
            } catch (e: Exception) {
                // En cas d'erreur technique (ex: problème d'accès à la DB)
                onError("Erreur lors de l'inscription : ${e.message}")
            }
        }
    }
}
