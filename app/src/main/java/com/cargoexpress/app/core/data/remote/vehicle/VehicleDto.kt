    package com.cargoexpress.app.core.data.remote.vehicle

    import com.cargoexpress.app.core.domain.Vehicle

    data class VehicleDto (
        val id: Int,
        val model: String,
        val plate: String,
        val tractorPlate: String,
        val maxLoad: Float,
        val volume: Float,
        val entrepreneurId: Int  // Esto es opcional, si es que está incluido en la respuesta del servidor

    )

    fun VehicleDto.toVehicle() = Vehicle(id,model,plate,tractorPlate,maxLoad,volume,entrepreneurId)