package com.cargoexpress.app.core.data.remote.user

import com.cargoexpress.app.core.data.remote.vehicle.VehicleDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EntrepreneurService {

    @GET("users/{userId}/entrepreneurs")
    suspend fun getEntrepreneurByUserId(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
    ): Response<EntrepreneurDto>

    @POST("entrepreneurs")
    suspend fun createEntrepreneur(@Body request: EntrepreneurRequestDto, @Header("Authorization") token: String): Response<EntrepreneurDto>

    @GET("entrepreneurs/{entrepreneurId}")
    suspend  fun getEntrepreneurById(@Path("entrepreneurId") id: Int, @Header("Authorization") token: String): Response<EntrepreneurDto>

    @GET("entrepreneurs")
    fun getEntrepreneurs(@Header("Authorization") token: String): Response<List<EntrepreneurDto>>

    @GET("entrepreneurs/{entrepreneurId}/vehicles")
    suspend fun getVehiclesEntrepreneurs(@Path("entrepreneurId") id: Int, @Header("Authorization") token: String): Response<List<VehicleDto>>


}