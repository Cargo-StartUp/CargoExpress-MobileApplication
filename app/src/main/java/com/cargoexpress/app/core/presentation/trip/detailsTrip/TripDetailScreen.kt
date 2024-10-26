package com.cargoexpress.app.core.presentation.trip.detailsTrip


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.domain.Trip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripCard(trip: Trip, navController: NavController) {
    val formattedDate = formatDateTime(trip.loadDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("trip_details/${trip.id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    text = "Viaje #${trip.id}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF999900),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Fecha de Carga: $formattedDate")
                Text(text = "Lugar de Carga: ${trip.loadLocation}")
            }
            IconButton(onClick = { /* Acción de edición */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TripDetailScreen(
    tripId: Int,
    navController: NavController,
    tripRepository: TripRepository
) {
    val factory = TripDetailViewModelFactory(tripRepository)
    val viewModel: TripDetailViewModel = viewModel(factory = factory)
    val trip by viewModel.trip.collectAsState()

    LaunchedEffect(tripId) {
        viewModel.loadTripDetails(tripId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        trip?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Trip Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF999900)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFCCCCCC))

                    DetailRow(label = "Nombre Vehículo", value = it.tripName)
                    DetailRow(label = "Tipo de carga", value = it.cargoType)
                    DetailRow(label = "Peso", value = "${it.weight} kg")
                    DetailRow(label = "Lugar de carga", value = it.loadLocation)
                    DetailRow(label = "Fecha de carga", value = formatDateTime(it.loadDate))
                    DetailRow(label = "Lugar de descarga", value = it.unloadLocation)
                    DetailRow(label = "Fecha de descarga", value = formatDateTime(it.unloadDate))
                    DetailRow(label = "Driver ID", value = it.driverId.toString())
                    DetailRow(label = "Vehicle ID", value = it.vehicleId.toString())
                    DetailRow(label = "Client ID", value = it.clientId.toString())
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: String): String {
    val parsedDateTime = LocalDateTime.parse(dateTime)
    return parsedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a"))
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF555555),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = Color(0xFF333333),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}