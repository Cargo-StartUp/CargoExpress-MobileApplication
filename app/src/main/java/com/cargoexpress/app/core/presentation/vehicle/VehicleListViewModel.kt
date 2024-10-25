package com.cargoexpress.app.core.presentation.vehicle

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository
import com.cargoexpress.app.core.data.repository.VehicleRepository
import com.cargoexpress.app.core.domain.Vehicle
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Resource
import pe.edu.upc.appturismo.common.UIState

class VehicleListViewModel(
    navController: NavHostController,
    private val vehicleRepository: VehicleRepository,
    entrepreneurRepository: EntrepreneurRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<Vehicle>>())
    val state: State<UIState<List<Vehicle>>> get() = _state

    fun getVehiclesForEntrepreneur(entrepreneurId: Int, token: String) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = vehicleRepository.getVehicleList(token)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else if (result is Resource.Error) {
                _state.value = UIState(message = result.message ?: "vuelve a nacer")
            }
        }
    }
}