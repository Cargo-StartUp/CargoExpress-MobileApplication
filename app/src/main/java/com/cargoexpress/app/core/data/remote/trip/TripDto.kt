package com.cargoexpress.app.core.data.remote.trip

import android.os.Build
import androidx.annotation.RequiresApi
import com.cargoexpress.app.core.domain.Trip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TripDto(
    val id: Int,
    val name: Name,
    val cargoData: CargoData,
    val tripData: TripData,
    val driverId: Int,
    val vehicleId: Int,
    val clientId: Int,
    val entrepreneurId: Int
)

data class Name(val tripName: String)
data class CargoData(val type: String, val weight: Int)
data class TripData(
    val loadLocation: String,
    val loadDate: String,
    val unloadLocation: String,
    val unloadDate: String
)

@RequiresApi(Build.VERSION_CODES.O)
private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

@RequiresApi(Build.VERSION_CODES.O)
fun TripDto.toTrip() = Trip(
    id = id,
    tripName = name.tripName,
    cargoType = cargoData.type,
    weight = cargoData.weight,
    loadLocation = tripData.loadLocation,
    loadDate = LocalDateTime.parse(tripData.loadDate, formatter).toString(),
    unloadLocation = tripData.unloadLocation,
    unloadDate = LocalDateTime.parse(tripData.unloadDate, formatter).toString(),
    driverId = driverId,
    vehicleId = vehicleId,
    clientId = clientId,
    entrepreneurId = entrepreneurId
)

fun TripDto.toTripLegacy(): Trip {

    return Trip(
        id = id,
        tripName = name.tripName,
        cargoType = cargoData.type,
        weight = cargoData.weight,
        loadLocation = tripData.loadLocation,
        loadDate = tripData.loadDate,
        unloadLocation = tripData.unloadLocation,
        unloadDate = tripData.unloadDate,
        driverId = driverId,
        vehicleId = vehicleId,
        clientId = clientId,
        entrepreneurId = entrepreneurId
    )
}