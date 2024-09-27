package com.cargoexpress.app.core.data.repository

import com.cargoexpress.app.core.data.remote.VehicleService
import com.cargoexpress.app.core.data.remote.toVehicle
import com.cargoexpress.app.core.domain.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Resource

class VehicleRepository(private val vehicleService: VehicleService) {

    suspend fun searchVehicleByVehicleId(vehicleId: Int,token: String): Resource<Vehicle> = withContext(Dispatchers.IO) {
        if(token.isBlank()){
            return@withContext Resource.Error(message = "Token is required")
        }

        val bearerToken = "Bearer $token"
        val response = vehicleService.getVehicle(vehicleId,bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { vehicleDto ->
                val vehicle = vehicleDto.toVehicle()
                return@withContext Resource.Success(vehicle)
            }
            return@withContext Resource.Error(message = "Vehicle not found")

        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getVehicleList(token: String): Resource<List<Vehicle>> = withContext(Dispatchers.IO){
        if (token.isBlank()){
            return@withContext Resource.Error(message = "Token is required")
        }
        val bearerToken = "Bearer $token"
        val response = vehicleService.getVehicles(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { vehicleDto ->
                val vehicle = vehicleDto.map() {it.toVehicle() }
                return@withContext Resource.Success(vehicle)
            }
            return@withContext Resource.Error(message = "Vehicles not found")

        }
        return@withContext Resource.Error(response.message())
    }
}

