package com.inviteme.vuemodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inviteme.exception.EvenementNonTrouveException
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu
import kotlinx.coroutines.launch

class VoirEvenementViewModel(application: Application) : AndroidViewModel(application) {

    val db = AppDatabase.getDatabase(application)

    val evenementDao = db.evenementDAO()
    val lieuDao = db.lieuDao()

    var evenement: Evenement? = null
    var lieu: Lieu? = null

    fun chargerEvenement(evenementId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                evenement = evenementDao.getById(evenementId)
                lieu = lieuDao.getById(evenement?.lieuId?.toInt() ?: -1)
                if (evenement == null) {
                    throw EvenementNonTrouveException("L'événement n'a pas été trouvé")
                }
                onSuccess()
            } catch (e: Exception) {
                onError("Erreur lors du chargement de l'événement: ${e.message}")
            }
        }
    }
}