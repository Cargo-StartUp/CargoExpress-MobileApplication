package com.cargoexpress.app.core.data.repository

import android.os.Build
import com.cargoexpress.app.core.data.remote.trip.TripService
import com.cargoexpress.app.core.data.remote.trip.toTrip
import com.cargoexpress.app.core.data.remote.trip.toTripLegacy
import com.cargoexpress.app.core.domain.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Resource

class TripRepository(private val tripService: TripService) {

    suspend fun getTrips(token: String): Resource<List<Trip>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }
        return@withContext try {
            val response = tripService.getTrips(token)
            if (response.isSuccessful) {
                val trips = response.body()?.map { tripDto ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tripDto.toTrip()
                    } else {
                        tripDto.toTripLegacy()
                    }
                } ?: emptyList()
                Resource.Success(data = trips)
            } else {
                Resource.Error(message = "Failed to fetch trips")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }
}