package com.cargoexpress.app.feature_auth.data.remote
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("authentication/sign-in")
    suspend fun signIn(@Body request: SignInRequest): Response<UserDto>

    @Headers("Content-Type: application/json")
    @POST("authentication/sign-up")
    suspend fun signUp(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<UserDto>
}