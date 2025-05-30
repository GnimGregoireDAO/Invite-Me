package com.inviteme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inviteme.model.entities.Evenement
import com.inviteme.model.repos.EvenementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvenementViewModel(private val repository: EvenementRepository) : ViewModel() {
    val evenements: LiveData<List<Evenement>> = repository.getAll()

    fun supprimerEvenement(evenement: Evenement) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.supprimerEvenement(evenement)
        }
    }

    fun getEvenementById(id: Int, callback: (Evenement?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = repository.getEvenementById(id)
            callback(event)
        }
    }    fun updateEvenement(evenement: Evenement, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvenement(evenement)
            onComplete()
        }
    }
}
