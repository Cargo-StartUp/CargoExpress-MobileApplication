package com.cargoexpress.app.core.data.remote.vehicle

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface VehicleService {

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: Int, @Header("Authorization") token: String): Response<VehicleDto>

    @GET("vehicles")
    suspend fun getVehicles(@Header("Authorization") token: String): Response<List<VehicleDto>>




}