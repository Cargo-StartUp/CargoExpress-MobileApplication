package com.cargoexpress.app.core.data.repository
import com.cargoexpress.app.core.data.remote.trip.TripService
import com.cargoexpress.app.core.data.remote.trip.toTrip
import com.cargoexpress.app.core.domain.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Resource
class TripRepository(private val tripService: TripService) {

    suspend fun getTrips(token: String): Resource<List<Trip>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }

        val response = tripService.getTrips(token)
        if (response.isSuccessful) {
            response.body()?.let { tripDtos ->
                val trips = tripDtos.map { it.toTrip() }
                return@withContext Resource.Success(trips)
            }
            return@withContext Resource.Error(message = "Trips not found")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getTrip(id: Int, token: String): Resource<Trip> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }

        val response = tripService.getTrip(id, token)
        if (response.isSuccessful) {
            response.body()?.let { tripDto ->
                val trip = tripDto.toTrip()
                return@withContext Resource.Success(trip)
            }
            return@withContext Resource.Error(message = "Trip not found")
        }
        return@withContext Resource.Error(response.message())
    }
}