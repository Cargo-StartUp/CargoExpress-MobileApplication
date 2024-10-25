package com.cargoexpress.app.core.data.remote.vehicle

import com.cargoexpress.app.core.data.remote.driver.DriverDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleService {

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: Int, @Header("Authorization") token: String): Response<VehicleDto>

    @GET("vehicles")
    suspend fun getVehicles(@Header("Authorization") token: String): Response<List<VehicleDto>>

    @POST("vehicles")
    suspend fun addVehicle(
        @Header("Authorization") token: String,
        @Body vehicle: VehicleDto
    ): Response<VehicleDto>

}