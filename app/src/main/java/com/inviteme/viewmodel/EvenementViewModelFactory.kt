package com.inviteme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inviteme.model.repos.EvenementRepository

class EvenementViewModelFactory(private val repository: EvenementRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EvenementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EvenementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
