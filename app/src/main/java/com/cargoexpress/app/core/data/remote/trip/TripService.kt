package com.cargoexpress.app.core.data.remote.trip


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TripService {
    @GET("trips")
    suspend fun getTrips(@Header("Authorization") token: String): Response<List<TripDto>>

    @GET("trips/{id}")
    suspend fun getTrip(@Path("id") id: Int, @Header("Authorization") token: String): Response<TripDto>
}