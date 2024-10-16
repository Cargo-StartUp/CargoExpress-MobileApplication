package com.cargoexpress.app.core.data.remote.trip

import com.cargoexpress.app.core.domain.Trip


class TripDto (
    val id: Int,
    val name: String,
    val weight: Int,
    val loadLocation: String,
    val loadDate: Float,
    val unloadLocation: Float,
    val unloadDate: Float,


)

fun TripDto.toTrip() = Trip(id,name,weight,loadLocation,loadDate,unloadLocation,unloadDate)