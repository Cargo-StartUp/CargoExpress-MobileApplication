package com.cargoexpress.app.core.presentation.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.domain.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Constants
import pe.edu.upc.appturismo.common.Resource
import pe.edu.upc.appturismo.common.UIState

class TripManagementViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<List<Trip>>(isLoading = true))
    val uiState: StateFlow<UIState<List<Trip>>> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            _uiState.value = UIState(isLoading = true)
            val result = tripRepository.getTrips(Constants.TOKEN)
            _uiState.value = when (result) {
                is Resource.Success -> UIState(data = result.data, isLoading = false)
                is Resource.Error -> UIState(isLoading = false, message = result.message ?: "Failed to load trips")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterTrips()
    }

    private fun filterTrips() {
        val query = _searchQuery.value
        val filteredTrips = _uiState.value.data?.filter { trip ->
            trip.id.toString().contains(query, ignoreCase = true) ||
                    trip.loadDate.toString().contains(query, ignoreCase = true) ||
                    trip.loadLocation.contains(query, ignoreCase = true)
        } ?: emptyList()
        _uiState.value = UIState(data = filteredTrips, isLoading = false)
    }

    fun handleError(exception: Exception) {
        val message = exception.message ?: "Unknown error"
        _uiState.value = UIState(isLoading = false, message = "Error: $message")
    }
}