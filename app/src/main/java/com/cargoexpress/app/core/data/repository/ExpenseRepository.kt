package com.cargoexpress.app.core.data.repository

import com.cargoexpress.app.core.data.remote.expense.ExpenseService
import com.cargoexpress.app.core.data.remote.expense.toExpense
import com.cargoexpress.app.core.data.remote.expense.toExpenseDto
import com.cargoexpress.app.core.domain.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.appturismo.common.Constants
import pe.edu.upc.appturismo.common.Resource

class ExpenseRepository(private val expenseService: ExpenseService) {

    suspend fun addExpense(expense: Expense): Resource<Expense> = withContext(Dispatchers.IO) {
        if (Constants.TOKEN.isBlank()) {
            return@withContext Resource.Error(message = "Token is required")
        }
        return@withContext try {
            val response = expenseService.addExpense("Bearer ${Constants.TOKEN}", expense.toExpenseDto())
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.toExpense() ?: expense)
            } else {
                Resource.Error(message = "Failed to add expense: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "An unknown error occurred")
        }
    }
}
