package com.cargoexpress.app.core.presentation.login

import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.cargoexpress.app.core.common.Routes
import com.cargoexpress.app.core.data.repository.LoginRepository
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Constants
import pe.edu.upc.appturismo.common.UIState

class LoginViewModel(
    private val navController: NavController,
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _state = MutableLiveData<UIState<Unit>>(UIState())
    val state: LiveData<UIState<Unit>> get() = _state

    fun signIn(username: String, password: String) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            loginRepository.signIn(username, password) { result ->
                result.onSuccess { loginResponse ->
                    Constants.USER_ID = loginResponse.id
                    Constants.TOKEN = loginResponse.token
                    Constants.USER_NAME = loginResponse.username
                    _state.value = UIState(data = Unit)
                    goToVehicleScreen()
                }.onFailure { exception ->
                    val message = exception.message ?: "Error desconocido"
                    _state.value = UIState(message = "Correo y/o contraseña incorrectos / $message")
                }
            }
        }
    }

    private fun goToVehicleScreen(){
        navController.navigate(Routes.VehicleList.routes)
    }
    private fun goToRegisterScreen(){
        navController.navigate(Routes.Register.routes)
    }

}