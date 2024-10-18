package com.cargoexpress.app.core.data.repository

import com.cargoexpress.app.core.data.remote.user.EntrepreneurDto
import com.cargoexpress.app.core.data.remote.user.EntrepreneurRequestDto
import com.cargoexpress.app.core.data.remote.user.EntrepreneurService
import com.cargoexpress.app.core.data.remote.vehicle.VehicleDto

class EntrepreneurRepository(private val entrepreneurService: EntrepreneurService) {

    suspend fun createEntrepreneur(request: EntrepreneurRequestDto, token: String): Result<Int> {
        return try {
            val response = entrepreneurService.createEntrepreneur(request, "Bearer $token")
            if (response.isSuccessful) {
                val entrepreneur = response.body()
                // Aquí verificamos que el entrepreneur no sea null y obtenemos el entrepreneurId
                entrepreneur?.let {
                    val entrepreneurId = it.id  // Almacenamos el entrepreneurId
                    Result.success(entrepreneurId)
                } ?: Result.failure(Exception("Error: No se pudo obtener el entrepreneurId"))
            } else {
                Result.failure(Exception("Error creando entrepreneur: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEntrepreneurByUserId(userId: Int, token: String): Result<EntrepreneurDto> {
        return try {
            val response = entrepreneurService.getEntrepreneurByUserId(userId, "Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)  // Retornar el EntrepreneurDto si está disponible
                } ?: Result.failure(Exception("Error: No se pudo obtener el empresario"))
            } else {
                Result.failure(Exception("Error obteniendo empresario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVehiclesByEntrepreneurId(id: Int, token: String): Result<List<VehicleDto>> {
        return try {
            val response = entrepreneurService.getVehiclesEntrepreneurs(id, "Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)  // La respuesta es ahora una lista
                } ?: Result.failure(Exception("Cuerpo de la respuesta vacío"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}