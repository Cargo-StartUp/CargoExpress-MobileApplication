package com.cargoexpress.app.core.data.repository



import com.cargoexpress.app.core.data.remote.driver.toDriver
import com.cargoexpress.app.core.data.remote.driver.toDriverDto
import com.cargoexpress.app.core.data.remote.vehicle.VehicleService
import com.cargoexpress.app.core.data.remote.vehicle.toVehicle
import com.cargoexpress.app.core.data.remote.vehicle.toVehicleDto
import com.cargoexpress.app.core.domain.Driver
import com.cargoexpress.app.core.domain.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Constants
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



    suspend fun addVehicle(vehicle: Vehicle): Resource<Vehicle> = withContext(Dispatchers.IO) {
        if (Constants.TOKEN.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }
        return@withContext try {
            val response = vehicleService.addVehicle("Bearer ${Constants.TOKEN}", vehicle.toVehicleDto())
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.toVehicle() ?: vehicle)
            } else {
                Resource.Error(message = "Failed to add vehicle")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }
}