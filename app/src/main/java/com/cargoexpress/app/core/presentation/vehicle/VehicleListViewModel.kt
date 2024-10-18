package com.cargoexpress.app.core.presentation.vehicle


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.cargoexpress.app.core.data.remote.vehicle.VehicleDto
import com.cargoexpress.app.core.data.remote.vehicle.toVehicle
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository
import com.cargoexpress.app.core.data.repository.VehicleRepository
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.UIState

class VehicleListViewModel(private val navController: NavController, private val vehicleRepository: VehicleRepository, private val entrepreneurRepository: EntrepreneurRepository )
    : ViewModel() {

    private val _state = mutableStateOf(UIState<List<VehicleDto>>())
    val state: State<UIState<List<VehicleDto>>> get() = _state

    private val _editVehicle = mutableStateOf<VehicleDto?>(null)
    val editVehicle: State<VehicleDto?> get() = _editVehicle


    fun goBack(){
        navController.popBackStack()
    }

    fun getVehiclesForEntrepreneur(entrepreneurId: Int, token: String) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = entrepreneurRepository.getVehiclesByEntrepreneurId(entrepreneurId, token)
            if (result.isSuccess) {
                val vehicleDtos = result.getOrNull()
                if (vehicleDtos != null && vehicleDtos.isNotEmpty()) {
                    // Aquí pasamos los VehicleDto directamente
                    _state.value = UIState(data = vehicleDtos)
                } else {
                    _state.value = UIState(message = "No vehicles found")
                }
            } else {
                _state.value = UIState(message = "Error retrieving vehicles")
            }
        }
    }



    /*

        fun getVehicleList(){
            _state.value = UIState(isLoading = true)
            viewModelScope.launch {
                var result = vehicleRepository.getVehicleList(Constants.TOKEN)
                if(result is Resource.Success){
                    val vehicles = result.data
                    if(vehicles != null){
                        val vehiclesInfo = mutableListOf<VehicleDto>()
                        for (v in vehicles){
                            val vehicleResult = vehicleRepository.searchVehicleByVehicleId(v.id,Constants.TOKEN)
                            if (vehicleResult is Resource.Success){
                                val vehicleId = vehicleResult.data?.id ?:0
                                val model = vehicleResult.data?.model ?: ""
                                val plate = vehicleResult.data?.plate ?: ""
                                val tractorPlate = vehicleResult.data?.tractorPlate ?: ""
                                val maxLoad = vehicleResult.data?.maxLoad ?:0.0f
                                val volume = vehicleResult.data?.volume ?:0.0f

                                vehiclesInfo.add(
                                    VehicleDto(
                                        id = vehicleId,
                                        model = model,
                                        plate = plate,
                                        tractorPlate = tractorPlate,
                                        maxLoad = maxLoad,
                                        volume = volume,

                                    )
                                )


                            }  else {
                                val vehicleId = vehicleResult.data?.id ?: 0
                                vehiclesInfo.add(
                                    VehicleDto(
                                        id = vehicleId,
                                        model = "",
                                        plate = "",
                                        tractorPlate = "",
                                        maxLoad = 0.0f,
                                        volume = 0.0f,
                                    )
                                )
                            }
                        }
                        _state.value = UIState(data = vehiclesInfo)
                    } else {
                        _state.value = UIState(message = "No vehicles found")

                    }
                } else if (result is Resource.Error){
                    _state.value = UIState(message = "Error with vehicles")
                }
            }
        }

        fun setEditVehicle(vehicle: VehicleDto) {
            _editVehicle.value = vehicle
        }

    */
}