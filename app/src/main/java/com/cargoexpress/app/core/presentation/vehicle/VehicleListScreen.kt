package com.cargoexpress.app.core.presentation.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cargoexpress.app.core.domain.Vehicle
import pe.edu.upc.appturismo.common.Constants

@Composable
fun VehicleListScreen(viewModel: VehicleListViewModel = viewModel(), navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getVehiclesForEntrepreneur(entrepreneurId = Constants.ENTREPRENEUR_ID, token = Constants.TOKEN)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .padding(end = 8.dp),
                    placeholder = { Text("Buscar vehículo") }
                )
                Button(
                    onClick = {
                        viewModel.getVehiclesForEntrepreneur(entrepreneurId = Constants.ENTREPRENEUR_ID, token = Constants.TOKEN)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F504)),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Buscar")
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.message?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
                val filteredVehicles = state.data?.filter { vehicle ->
                    vehicle.model.contains(searchQuery, ignoreCase = true) ||
                            vehicle.plate.contains(searchQuery, ignoreCase = true) ||
                            vehicle.tractorPlate.contains(searchQuery, ignoreCase = true)
                } ?: emptyList()

                if (filteredVehicles.isEmpty()) {
                    item {
                        Text(
                            text = "",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    items(filteredVehicles.size) { index ->
                        val vehicle = filteredVehicles[index]
                        VehicleItem(vehicle = vehicle)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {navController.navigate("register_vehicle?token=${Constants.TOKEN}") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFF1F504)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun VehicleItem(vehicle: Vehicle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A3A3A),
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Modelo: ${vehicle.model}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    text = "Placa: ${vehicle.plate}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "Placa del tractor: ${vehicle.tractorPlate}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "Carga máxima: ${vehicle.maxLoad} kg",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = "Volumen: ${vehicle.volume} m³",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
            IconButton(onClick = {/*EDITAR*/ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}