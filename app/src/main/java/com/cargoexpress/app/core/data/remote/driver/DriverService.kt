package com.cargoexpress.app.core.data.remote.driver

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DriverService {
    @GET("drivers")
    suspend fun getDrivers(@Header("Authorization") token: String): Response<List<DriverDto>>
}