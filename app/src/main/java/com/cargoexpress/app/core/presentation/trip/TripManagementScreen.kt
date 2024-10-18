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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.domain.Trip

@Composable
fun TripManagementScreen(
    tripRepository: TripRepository
) {
    val factory = remember { TripManagementViewModelFactory(tripRepository) }
    val viewModel: TripManagementViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ID") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    viewModel.updateSearchQuery(searchQuery, selectedFilter)
                },
                onSearchClick = {
                    viewModel.updateSearchQuery(searchQuery, selectedFilter)
                }
            )


            Button(
                onClick = {
                    viewModel.updateSearchQuery(searchQuery, selectedFilter)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Buscar")
            }
        }

        FilterOptions(
            selectedFilter = selectedFilter,
            onFilterChange = { selectedFilter = it }
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            uiState.message.isNotBlank() -> {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                TripList(trips = uiState.data ?: emptyList())
            }
        }
    }
}

@Composable
fun FilterOptions(selectedFilter: String, onFilterChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("ID", "Fecha", "Lugar").forEach { filter ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                RadioButton(
                    selected = selectedFilter == filter,
                    onClick = { onFilterChange(filter) }
                )
                Text(text = filter)
            }
        }
    }
}

@Composable
fun TripList(trips: List<Trip>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trips) { trip ->
            TripCard(trip = trip)
        }
    }
}

@Composable
fun TripCard(trip: Trip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Viaje #${trip.id}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF999900)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "FECHA DE CARGA: ${trip.loadDate}")
            Text(text = "LUGAR DE CARGA: ${trip.loadLocation}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFF00)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Ver más")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Button(
            onClick = onSearchClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFF00)),
            modifier = Modifier.height(56.dp)
        ) {
            Text(
                text = "Buscar",
                color = Color.Black
            )
        }
    }
}
