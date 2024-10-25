package com.cargoexpress.app.core.data.remote.expense

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Body

interface ExpenseService {
    @POST("expenses")
    suspend fun addExpense(
        @Header("Authorization") token: String,
        @Body expense: ExpenseDto
    ): Response<ExpenseDto>
}
