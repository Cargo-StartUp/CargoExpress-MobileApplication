package com.cargoexpress.app.core.data.repository

import com.cargoexpress.app.core.data.remote.driver.DriverService
import com.cargoexpress.app.core.data.remote.driver.toDriver
import com.cargoexpress.app.core.domain.Driver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Resource

class DriverRepository(private val driverService: DriverService) {

    suspend fun getDrivers(token: String): Resource<List<Driver>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }
        return@withContext try {
            val response = driverService.getDrivers("Bearer $token")
            if (response.isSuccessful) {
                val drivers = response.body()?.map { it.toDriver() } ?: emptyList()
                Resource.Success(data = drivers)
            } else {
                Resource.Error(message = "Failed to fetch drivers")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }
}