package com.cargoexpress.app.core.data.remote.vehicle

import com.cargoexpress.app.core.domain.Vehicle

class VehicleDto (
    val id: Int,
    val model: String,
    val plate: String,
    val tractorPlate: String,
    val maxLoad: Float,
    val volume: Float
)

fun VehicleDto.toVehicle() = Vehicle(id,model,plate,tractorPlate,maxLoad,volume)