package com.cargoexpress.app.core.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.cargoexpress.app.core.common.Routes
import com.cargoexpress.app.core.data.remote.user.EntrepreneurRequestDto
import com.cargoexpress.app.core.data.repository.LoginRepository
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Constants
import pe.edu.upc.appturismo.common.UIState

class LoginViewModel(
    private val navController: NavController,
    private val loginRepository: LoginRepository,
    private val entrepreneurRepository: EntrepreneurRepository  // Repositorio para obtener el ID del empresario
) : ViewModel() {

    private val _state = MutableLiveData<UIState<Unit>>(UIState())
    val state: LiveData<UIState<Unit>> get() = _state

    // Función de inicio de sesión
    fun signIn(username: String, password: String) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            loginRepository.signIn(username, password) { result ->
                result.onSuccess { loginResponse ->
                    // Guardar el userId y token en los Constants
                    Constants.USER_ID = loginResponse.id
                    Constants.TOKEN = loginResponse.token
                    Constants.USER_NAME = loginResponse.username

                    // Obtener el entrepreneurId usando el LoginResponse y el token
                    getEntrepreneurIdForUser(loginResponse.id, loginResponse.token)
                }.onFailure { exception ->
                    val message = exception.message ?: "Error desconocido"
                    _state.value = UIState(message = "Correo y/o contraseña incorrectos / $message")
                }
            }
        }
    }

    // Función para obtener el entrepreneurId usando el EntrepreneurRepository
    private fun getEntrepreneurIdForUser(userId: Int, token: String) {
        viewModelScope.launch {
            entrepreneurRepository.getEntrepreneurByUserId(
                userId = userId.toInt(),  // Convertir el userId a Int
                token = token
            ).onSuccess { entrepreneur ->
                // Guardar el entrepreneurId en los Constants
                Constants.ENTREPRENEUR_ID = entrepreneur.id

                // Navegar a la pantalla de vehículos después de obtener el entrepreneurId
                goToVehicleScreen()
            }.onFailure { exception ->
                val message = exception.message ?: "Error obteniendo el ID del empresario"
                _state.value = UIState(message = message)
            }
        }
    }

    // Navegar a la pantalla de la lista de vehículos
    private fun goToVehicleScreen() {
        navController.navigate(Routes.VehicleList.routes)
    }

    private fun goToRegisterScreen() {
        navController.navigate(Routes.Register.routes)
    }
}
