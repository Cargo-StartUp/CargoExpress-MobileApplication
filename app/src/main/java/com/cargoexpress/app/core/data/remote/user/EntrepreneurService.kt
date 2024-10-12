package com.cargoexpress.app.core.data.remote.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EntrepreneurService {

    @POST("entrepreneurs")
    suspend fun createEntrepreneur(@Body request: EntrepreneurRequestDto, @Header("Authorization") token: String): Response<EntrepreneurDto>

    @GET("entrepreneurs/{id}")
    fun getEntrepreneur(@Path("id") id: Int, @Header("Authorization") token: String): Response<EntrepreneurDto>

    @GET("entrepreneurs")
    fun getEntrepreneurs(@Header("Authorization") token: String): Response<List<EntrepreneurDto>>
}