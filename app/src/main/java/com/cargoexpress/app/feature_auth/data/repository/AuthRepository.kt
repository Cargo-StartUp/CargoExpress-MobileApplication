package com.cargoexpress.app.feature_auth.data.repository

import android.util.Log
import com.cargoexpress.app.feature_auth.common.Resource
import com.cargoexpress.app.feature_auth.data.remote.AuthService
import com.cargoexpress.app.feature_auth.data.remote.SignInRequest
import com.cargoexpress.app.feature_auth.data.remote.toUser
import com.cargoexpress.app.feature_auth.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val authService: AuthService) {
    suspend fun signIn(username: String, password: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val request = SignInRequest(username, password)
            Log.d("AuthRepository", "SignIn request: $request")
            val response = authService.signIn(request)
            if (response.isSuccessful) {
                val user = response.body()?.toUser()
                if (user != null) {
                    Resource.Success(user)
                } else {
                    Resource.Error("Usuario o contraseña incorrecta")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "SignIn error: $errorBody")
                Resource.Error("Error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun signUp(username: String, password: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val response = authService.signUp(username, password)
            if (response.isSuccessful) {
                val user = response.body()?.toUser()
                if (user != null) {
                    Resource.Success(user)
                } else {
                    Resource.Error("User not found")
                }
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}