package com.cargoexpress.app.core.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository

class ProfileViewModelFactory(
    private val entrepreneurRepository: EntrepreneurRepository
) : ViewModelProvider.Factory {
     fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(entrepreneurRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}