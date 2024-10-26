package com.cargoexpress.app.core.data.remote.trip


import com.cargoexpress.app.core.data.remote.expense.ExpenseDto
import com.cargoexpress.app.core.domain.Expense
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST

interface TripService {
    @GET("trips")
    suspend fun getTrips(@Header("Authorization") token: String): Response<List<TripDto>>

    @GET("trips/{id}")
    suspend fun getTrip(@Path("id") id: Int, @Header("Authorization") token: String): Response<TripDto>


    @POST("trips")
    suspend fun addTrip(
        @Header("Authorization") token: String,
        @Body trip: TripDtoPost
    ): Response<TripDtoPost>

    @POST("expenses")
    suspend fun addExpense(
        @Header("Authorization") token: String,
        @Body expense: ExpenseDto
    ): Response<Expense>

    @GET("trips/{tripId}/expense")
    suspend fun getExpenseByTripId(
        @Path("tripId") tripId: Int,
        @Header("Authorization") token: String
    ): Response<Expense>

}