package com.inviteme.vuemodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inviteme.exception.EvenementNonConformeException
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu
import com.inviteme.model.repos.EvenementRepository
import com.inviteme.states.EvenmentState
import com.inviteme.states.LieuState
import kotlinx.coroutines.launch
import java.sql.Timestamp

class AjouterEvementVueModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val evenementDao = db.evenementDAO()
    private val lieuDao = db.lieuDao()

    val evenementState = EvenmentState()
    val lieuState = LieuState()

    fun ajouterEvenement(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validateFields()) {
            onError("Champs obligatoires manquants")
            return
        }

        viewModelScope.launch {
            try {
                val lieuId = lieuDao.insert(createLieu())
                evenementDao.addEvenement(createEvenement(lieuId.toInt()))
                onSuccess()
            } catch (e: Exception) {
                Log.e("ViewModel", "Erreur DB", e)
                onError("Erreur: ${e.localizedMessage}") // on affiche le message d'erreur
            }
        }
    }

    // valider les champs importants: ici titre, date et adresse
    private fun validateFields(): Boolean {
        return !evenementState.titre.isNullOrBlank() &&
                evenementState.date != null &&
                !lieuState.adresse.isNullOrBlank()
    }

    // créer un lieu à partir de l'état du
    private fun createLieu(): Lieu {
        return Lieu(
            adresse = lieuState.adresse!!,
            pays = "France" // donc oui, je suis coincé en france hahaha
        )
    }

    // ajouter un evement à partir de la l'id du lieu qui aura été ajouté
    private fun createEvenement(lieuId: Int): Evenement {
        return Evenement(
            id = 0,
            titre = evenementState.titre!!,
            description = evenementState.description,
            dateAjout = evenementState.date!!,
            dateModification = Timestamp(System.currentTimeMillis()), // Génération automatique
            lieuId = lieuId.toLong()
        )
    }
}
