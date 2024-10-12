package com.cargoexpress.app.core.data.repository

import com.cargoexpress.app.core.data.remote.user.EntrepreneurDto
import com.cargoexpress.app.core.data.remote.user.EntrepreneurRequestDto
import com.cargoexpress.app.core.data.remote.user.EntrepreneurService

class EntrepreneurRepository(private val entrepreneurService: EntrepreneurService) {

    suspend fun createEntrepreneur(request: EntrepreneurRequestDto, token: String): Result<EntrepreneurDto> {
        return try {
            val response = entrepreneurService.createEntrepreneur(request, "Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error creando entrepreneur: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getEntrepreneur(id: Int, token: String): Result<EntrepreneurDto> {
        return try {
            val response = entrepreneurService.getEntrepreneur(id, token)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error obteniendo empresario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getEntrepreneurs(token: String): Result<List<EntrepreneurDto>> {
        return try {
            val response = entrepreneurService.getEntrepreneurs(token)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error obteniendo empresarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}