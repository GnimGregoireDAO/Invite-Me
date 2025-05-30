package com.inviteme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Utilisateur
import com.inviteme.model.repos.UtilisateurRepository
import kotlinx.coroutines.launch

// ViewModel : sert d’intermédiaire entre l’interface utilisateur (UI) et la base de données
class ConnexionViewModel(application: Application) : AndroidViewModel(application) {

    // Référence au repository qui interagit avec la base de données via la DAO
    private val repository: UtilisateurRepository

    // Bloc d’initialisation, exécuté automatiquement à la création du ViewModel
    init {
        // Récupère la DAO depuis la base de données
        val utilisateurDao = AppDatabase.getDatabase(application).utilisateurDao()
        // Initialise le repository avec la DAO
        repository = UtilisateurRepository(utilisateurDao)
    }

    /**
     * Fonction qui tente de connecter un utilisateur
     *
     * @param email Email saisi par l'utilisateur
     * @param password Mot de passe saisi par l'utilisateur
     * @param onSuccess Fonction appelée si la connexion est réussie (utilisateur trouvé + bon mot de passe)
     * @param onError Fonction appelée si erreur (utilisateur non trouvé, mot de passe incorrect ou erreur technique)
     */
    fun connecter(
        email: String,
        password: String,
        onSuccess: (Utilisateur) -> Unit,
        onError: (String) -> Unit
    ) {
        // Lancement d’une coroutine (asynchrone) dans le scope du ViewModel
        viewModelScope.launch {
            try {
                // Tente de récupérer un utilisateur à partir de l’email
                val utilisateur = repository.getParEmail(email)

                if (utilisateur == null) {
                    // Si aucun utilisateur trouvé → message d’erreur
                    onError("Aucun compte trouvé avec cet e-mail.")
                } else {
                    // Si utilisateur trouvé → on vérifie si le mot de passe correspond
                    if (utilisateur.password == password) {
                        // Mot de passe correct → succès
                        onSuccess(utilisateur)
                    } else {
                        // Mot de passe incorrect
                        onError("Mot de passe incorrect.")
                    }
                }
            } catch (e: Exception) {
                // Gestion des erreurs techniques (ex : base de données inaccessible)
                onError("Erreur lors de la connexion : ${e.message}")
            }
        }
    }
}
