package com.cargoexpress.app.core.presentation.trip.registerTrip

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.core.domain.Trip
import kotlinx.coroutines.launch
import pe.edu.upc.appturismo.common.Constants
import pe.edu.upc.appturismo.common.Resource
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterTripScreen(
    viewModel: RegisterTripViewModel = viewModel(),
    onTripRegistered: (Trip) -> Unit
) {
    var tripName by remember { mutableStateOf("") }
    var cargoType by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var loadLocation by remember { mutableStateOf("") }
    var loadDate by remember { mutableStateOf("") }
    var loadTime by remember { mutableStateOf("") }
    var unloadLocation by remember { mutableStateOf("") }
    var unloadDate by remember { mutableStateOf("") }
    var unloadTime by remember { mutableStateOf("") }
    var driverId by remember { mutableStateOf("") }
    var vehicleId by remember { mutableStateOf("") }
    var clientId by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var tripNameError by remember { mutableStateOf<String?>(null) }
    var cargoTypeError by remember { mutableStateOf<String?>(null) }
    var weightError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val context = LocalContext.current

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelected(timeFormatter.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            RegisterTripButton(
                isLoading = isLoading,
                onClick = {
                    tripNameError = if (tripName.isBlank()) "El nombre del viaje es obligatorio" else null
                    cargoTypeError = if (cargoType.isBlank()) "El tipo de carga es obligatorio" else null
                    weightError = if (weight.isBlank() || weight.toFloatOrNull() == null) "El peso debe ser un número" else null

                    val isValid = listOf(tripNameError, cargoTypeError, weightError).all { it == null }

                    if (isValid) {
                        isLoading = true
                        viewModel.apply {
                            this.tripName = tripName
                            this.cargoType = cargoType
                            this.weight = weight.toIntOrNull() ?: 0
                            this.loadLocation = loadLocation
                            this.loadDate = isoFormatter.format(dateFormatter.parse(loadDate)!!).substring(0, 10) + "T$loadTime:00"
                            this.unloadLocation = unloadLocation
                            this.unloadDate = isoFormatter.format(dateFormatter.parse(unloadDate)!!).substring(0, 10) + "T$unloadTime:00"
                            this.driverId = driverId.toIntOrNull() ?: 0
                            this.vehicleId = vehicleId.toIntOrNull() ?: 0
                            this.clientId = clientId.toIntOrNull() ?: 0
                        }

                        viewModel.registerTrip { result ->
                            isLoading = false
                            val message = if (result is Resource.Success && result.data != null) {
                                onTripRegistered(result.data)
                                resetFields(
                                    setTripName = { tripName = it },
                                    setCargoType = { cargoType = it },
                                    setWeight = { weight = it },
                                    setLoadLocation = { loadLocation = it },
                                    setLoadDate = { loadDate = it },
                                    setLoadTime = { loadTime = it },
                                    setUnloadLocation = { unloadLocation = it },
                                    setUnloadDate = { unloadDate = it },
                                    setUnloadTime = { unloadTime = it },
                                    setDriverId = { driverId = it },
                                    setVehicleId = { vehicleId = it },
                                    setClientId = { clientId = it }
                                )
                                "Viaje registrado exitosamente"
                            } else {
                                "Error al registrar el viaje: ${result.message}"
                            }

                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Por favor complete todos los campos correctamente") }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderText("Registrar Nuevo Viaje")

            InputField(value = tripName, label = "Nombre del Viaje", onValueChange = { tripName = it }, error = tripNameError)
            InputField(value = cargoType, label = "Tipo de Carga", onValueChange = { cargoType = it }, error = cargoTypeError)
            InputField(value = weight, label = "Peso", onValueChange = { weight = it }, error = weightError)

            LocationPicker("Ubicación de Carga", loadLocation) { loadLocation = it }
            InputFieldWithDatePicker(value = loadDate, label = "Fecha de Carga", onValueChange = { loadDate = it }, onDateSelected = { loadDate = it })
            InputFieldWithTimePicker(value = loadTime, label = "Hora de Carga", onValueChange = { loadTime = it }, onTimeSelected = { loadTime = it })

            LocationPicker("Ubicación de Descarga", unloadLocation) { unloadLocation = it }
            InputFieldWithDatePicker(value = unloadDate, label = "Fecha de Descarga", onValueChange = { unloadDate = it }, onDateSelected = { unloadDate = it })
            InputFieldWithTimePicker(value = unloadTime, label = "Hora de Descarga", onValueChange = { unloadTime = it }, onTimeSelected = { unloadTime = it })

            InputField(value = driverId, label = "ID del Conductor", onValueChange = { driverId = it })
            InputField(value = vehicleId, label = "ID del Vehículo", onValueChange = { vehicleId = it })
            InputField(value = clientId, label = "ID del Cliente", onValueChange = { clientId = it })

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFFFFEB3B),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun RegisterTripButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B)),
        enabled = !isLoading
    ) {
        Text("Registrar Viaje", color = Color.Black, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun HeaderText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFFFEB3B),
                unfocusedIndicatorColor = Color.Gray
            ),
            isError = error != null
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun LocationPicker(label: String, location: String, onValueChange: (String) -> Unit) {
    InputField(
        value = location,
        label = label,
        onValueChange = onValueChange
    )
}

fun resetFields(
    setTripName: (String) -> Unit,
    setCargoType: (String) -> Unit,
    setWeight: (String) -> Unit,
    setLoadLocation: (String) -> Unit,
    setLoadDate: (String) -> Unit,
    setLoadTime: (String) -> Unit,
    setUnloadLocation: (String) -> Unit,
    setUnloadDate: (String) -> Unit,
    setUnloadTime: (String) -> Unit,
    setDriverId: (String) -> Unit,
    setVehicleId: (String) -> Unit,
    setClientId: (String) -> Unit
) {
    setTripName("")
    setCargoType("")
    setWeight("")
    setLoadLocation("")
    setLoadDate("")
    setLoadTime("")
    setUnloadLocation("")
    setUnloadDate("")
    setUnloadTime("")
    setDriverId("")
    setVehicleId("")
    setClientId("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldWithDatePicker(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    error: String? = null
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker(onDateSelected) }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar Fecha")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFFFEB3B),
                unfocusedIndicatorColor = Color.Gray
            ),
            isError = error != null
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldWithTimePicker(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    error: String? = null
) {
    val context = LocalContext.current
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelected(timeFormatter.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showTimePicker(onTimeSelected) }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Seleccionar Hora")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFFFEB3B),
                unfocusedIndicatorColor = Color.Gray
            ),
            isError = error != null
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}