package com.cargoexpress.app.core.presentation.trip



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import pe.edu.upc.appturismo.common.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun TripManagementScreen(
    tripRepository: TripRepository,
    navController: NavController
) {
    val factory = remember { TripManagementViewModelFactory(tripRepository) }
    val viewModel: TripManagementViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    var isAscending by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isAscending = !isAscending
                        viewModel.updateSortOrder(isAscending)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort")
                }

                IconButton(
                    onClick = {
                        showDatePicker(context) { date ->
                            selectedDate = date
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Filter by Date")
                }

                Button(
                    onClick = {
                        viewModel.updateSearchQuery(selectedDate, "Fecha")
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Aceptar")
                }
            }

            FilterOptions(
                selectedFilter = "Fecha",
                onFilterChange = { /* Lógica para cambiar el filtro */ }
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
                    TripList(trips = uiState.data ?: emptyList(), navController = navController)
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("register_trip?token=${Constants.TOKEN}") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFF1F504)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val selectedDate = String.format("%04d-%02d-%02dT00:00:00", year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
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
fun TripList(trips: List<Trip>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(trips) { trip ->
            TripCard(trip = trip, navController = navController)
        }
    }
}

@Composable
fun TripCard(trip: Trip, navController: NavController) {
    val parsedDateTime = LocalDateTime.parse(trip.loadDate)
    val formattedDate = parsedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                    color = Color(0xFF999900)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "FECHA DE CARGA: $formattedDate")
                Text(text = "LUGAR DE CARGA: ${trip.loadLocation}")
            }
            IconButton(onClick = { /*EDITAR*/ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
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