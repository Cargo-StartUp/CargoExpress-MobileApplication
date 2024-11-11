package com.cargoexpress.app.core.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Constants

data class UserProfile(val name: String, val email: String, val phone: String)

class ProfileViewModel(private val entrepreneurRepository: EntrepreneurRepository) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            val token = Constants.TOKEN
            val result = entrepreneurRepository.getEntrepreneurByUserId(userId, token)
            result.onSuccess { entrepreneurDto ->
                _userProfile.value = UserProfile(
                    name = entrepreneurDto.name,
                    email = entrepreneurDto.address,
                    phone = entrepreneurDto.phone
                )
            }.onFailure { exception ->

                _userProfile.value = null
            }
        }
    }
}