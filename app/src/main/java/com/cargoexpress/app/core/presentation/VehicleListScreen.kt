package com.cargoexpress.app.core.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.core.data.remote.VehicleDto

@Composable
fun VehicleListScreen(viewModel: VehicleListViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getVehicleList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                placeholder = { Text("Buscar vehículo por modelo o placa") }
            )
            Button(onClick = {
                viewModel.getVehicleList()
            }) {
                Text("Buscar")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.message?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            val filteredVehicles = state.data?.filter { vehicle ->
                vehicle.model.contains(searchQuery, ignoreCase = true) ||
                        vehicle.plate.contains(searchQuery, ignoreCase = true)
            } ?: emptyList()

            items(filteredVehicles.size) { index ->
                val vehicle = filteredVehicles[index]
                VehicleItem(vehicle = vehicle)
            }
        }
    }
}

@Composable
fun VehicleItem(vehicle: VehicleDto) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Modelo: ${vehicle.model}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Placa: ${vehicle.plate}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Placa del tractor: ${vehicle.tractorPlate}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Carga máxima: ${vehicle.maxLoad} kg", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Volumen: ${vehicle.volume} m³", style = MaterialTheme.typography.bodyLarge)
        }
    }
}