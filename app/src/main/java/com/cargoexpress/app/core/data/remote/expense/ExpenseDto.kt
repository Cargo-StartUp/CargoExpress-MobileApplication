package com.cargoexpress.app.core.data.remote.expense

import com.cargoexpress.app.core.domain.Expense

data class ExpenseDto(
    val id: Int,
    val fuelAmount: Double,
    val fuelDescription: String,
    val viaticsAmount: Double,
    val viaticsDescription: String,
    val tollsAmount: Double,
    val tollsDescription: String,
    val tripId: Int
)

fun ExpenseDto.toExpense() = Expense(
    id = id,
    fuelAmount = fuelAmount,
    fuelDescription = fuelDescription,
    viaticsAmount = viaticsAmount,
    viaticsDescription = viaticsDescription,
    tollsAmount = tollsAmount,
    tollsDescription = tollsDescription,
    tripId = tripId
)

fun Expense.toExpenseDto() = ExpenseDto(
    id = id,
    fuelAmount = fuelAmount,
    fuelDescription = fuelDescription,
    viaticsAmount = viaticsAmount,
    viaticsDescription = viaticsDescription,
    tollsAmount = tollsAmount,
    tollsDescription = tollsDescription,
    tripId = tripId
)