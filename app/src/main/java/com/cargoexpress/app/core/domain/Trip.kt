package com.cargoexpress.app.core.domain

data class Trip(
    val id: Int,
    val name: String,
    val weight: Int,
    val loadLocation: String,
    val loadDate: Float,
    val unloadLocation: Float,
    val unloadDate: Float,
)
