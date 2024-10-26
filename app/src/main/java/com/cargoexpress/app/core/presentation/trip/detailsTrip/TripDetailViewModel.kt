package com.cargoexpress.app.core.presentation.trip.detailsTrip


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.domain.Expense
import com.cargoexpress.app.core.domain.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Resource

class TripDetailViewModel(private val repository: TripRepository) : ViewModel() {
    private val _trip = MutableStateFlow<Trip?>(null)
    val trip: StateFlow<Trip?> get() = _trip

    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> get() = _expense

    fun loadTripDetails(tripId: Int) {
        viewModelScope.launch {
            when (val result = repository.getTripById(tripId)) {
                is Resource.Success -> _trip.value = result.data
                is Resource.Error -> _trip.value = null
            }
        }
    }

    fun loadExpenseDetails(tripId: Int) {
        viewModelScope.launch {
            when (val result = repository.getExpenseByTripId(tripId)) {
                is Resource.Success -> _expense.value = result.data
                is Resource.Error -> _expense.value = null // Handle error appropriately
            }
        }
    }

    /*fun addExpense(tripId: Int, onExpenseAdded: (Expense) -> Unit) {
        viewModelScope.launch {
            val expense = repository.addExpense(tripId)
            _expense.value = expense
            onExpenseAdded(expense)
        }
    }*/
}