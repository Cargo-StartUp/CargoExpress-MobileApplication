package com.cargoexpress.app.core.presentation.vehicle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.R
import com.cargoexpress.app.core.data.remote.vehicle.VehicleDto
import pe.edu.upc.appturismo.common.Constants

@Composable
fun VehicleListScreen(viewModel: VehicleListViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getVehicleList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        HeaderSection()
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
                    .padding(end = 8.dp)
                    .shadow(4.dp, RoundedCornerShape(24.dp)),
                placeholder = { Text("Buscar vehículo ") }
            )
            Button(
                onClick = {
                    viewModel.getVehicleList()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F504)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(8.dp)
            ) {
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
        Column(modifier = Modifier.padding(16.dp)) {
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

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F500),
                    contentColor = Color.Black
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Ver más")
            }
        }
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))


        Text(
            text = "CargoExpress",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        AdminBadge()
    }
}


@Composable
fun AdminBadge() {
    val userName = remember { Constants.USER_NAME }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color(0xFF3A3A3A)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.bodySmall,

            color = Color.Yellow,
            modifier = Modifier.padding(15.dp)
        )
    }
}