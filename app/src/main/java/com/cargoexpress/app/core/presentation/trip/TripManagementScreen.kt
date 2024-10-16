package com.cargoexpress.app.core.presentation.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.cargoexpress.app.core.data.remote.trip.TripDto
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.domain.Trip

@Composable
fun TripManagementScreen(viewModel: TripManagementViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            uiState.message.isNotBlank() -> {
                Text(text = uiState.message, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            else -> {
                TripList(trips = uiState.data ?: emptyList(), searchQuery = searchQuery)
            }
        }
    }
}

@Composable
fun TripList(trips: List<Trip>, searchQuery: String) {
    val filteredTrips = trips.filter { trip ->
        trip.id.toString().contains(searchQuery, ignoreCase = true) ||
                trip.loadDate.toString().contains(searchQuery, ignoreCase = true) ||
                trip.loadLocation.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredTrips) { trip ->
            TripCard(trip = trip)
        }
    }
}

@Composable
fun TripCard(trip: Trip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Viaje #${trip.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Yellow
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fecha de carga: ${trip.loadDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Lugar de carga: ${trip.loadLocation}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {  },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Text(text = "Ver más", color = Color.Black)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Buscar") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Yellow,
            unfocusedIndicatorColor = Color.Gray,

        )
    )
}


/*@Preview(showBackground = true)
@Composable
fun TripManagementScreenPreview() {
    val navController = rememberNavController()
    val viewModel = TripManagementViewModel(
        tripRepository = FakeTripRepository()
    )
    TripManagementScreen(viewModel = viewModel)
}

class FakeTripRepository : TripRepository {
    override suspend fun getTrips(token: String): List<TripDto> {
        return listOf(
            TripDto(1, "Viaje 1", 100, "Lima", 100f, 200f),
            TripDto(2, "Viaje 2", 200, "Arequipa", 200f, 300f),
            TripDto(3, "Viaje 3", 300, "Cusco", 300f, 400f),
        )
    }
}
*/