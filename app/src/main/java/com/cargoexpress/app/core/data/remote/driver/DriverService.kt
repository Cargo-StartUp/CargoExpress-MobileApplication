package com.cargoexpress.app.core.data.remote.driver

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Body


interface DriverService {
    @GET("drivers")
    suspend fun getDrivers(@Header("Authorization") token: String): Response<List<DriverDto>>

    @POST("drivers")
    suspend fun addDriver(
        @Header("Authorization") token: String,
        @Body driver: DriverDto
    ): Response<DriverDto>
}