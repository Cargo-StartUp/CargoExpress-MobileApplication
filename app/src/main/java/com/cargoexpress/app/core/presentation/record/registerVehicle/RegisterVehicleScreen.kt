package com.cargoexpress.app.core.presentation.record.registerVehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.core.domain.Vehicle
import pe.edu.upc.appturismo.common.Resource
import kotlinx.coroutines.launch

@Composable
fun RegisterVehicleScreen(
    viewModel: RegisterVehicleViewModel = viewModel(),
    onVehicleRegistered: (Vehicle) -> Unit
) {
    var model by remember { mutableStateOf(viewModel.model) }
    var plate by remember { mutableStateOf(viewModel.plate) }
    var tractorPlate by remember { mutableStateOf(viewModel.tractorPlate) }
    var maxLoad by remember { mutableStateOf(viewModel.maxLoad.toString()) }
    var volume by remember { mutableStateOf(viewModel.volume.toString()) }

    var modelError by remember { mutableStateOf<String?>(null) }
    var plateError by remember { mutableStateOf<String?>(null) }
    var tractorPlateError by remember { mutableStateOf<String?>(null) }
    var maxLoadError by remember { mutableStateOf<String?>(null) }
    var volumeError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                value = model,
                label = "Model",
                onValueChange = {
                    model = it
                    modelError = if (it.isBlank()) "Model is required" else null
                },
                error = modelError
            )
            InputField(
                value = plate,
                label = "Plate",
                onValueChange = {
                    plate = it
                    plateError = if (it.isBlank()) "Plate is required" else null
                },
                error = plateError
            )
            InputField(
                value = tractorPlate,
                label = "Tractor Plate",
                onValueChange = {
                    tractorPlate = it
                    tractorPlateError = if (it.isBlank()) "Tractor Plate is required" else null
                },
                error = tractorPlateError
            )
            InputField(
                value = maxLoad,
                label = "Max Load",
                onValueChange = {
                    maxLoad = it
                    maxLoadError = if (it.isBlank() || it.toFloatOrNull() == null) "Max Load is required and must be a number" else null
                },
                error = maxLoadError
            )
            InputField(
                value = volume,
                label = "Volume",
                onValueChange = {
                    volume = it
                    volumeError = if (it.isBlank() || it.toFloatOrNull() == null) "Volume is required and must be a number" else null
                },
                error = volumeError
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFFEB3B))
            }

            Button(
                onClick = {
                    modelError = if (model.isBlank()) "Model is required" else null
                    plateError = if (plate.isBlank()) "Plate is required" else null
                    tractorPlateError = if (tractorPlate.isBlank()) "Tractor Plate is required" else null
                    maxLoadError = if (maxLoad.isBlank() || maxLoad.toFloatOrNull() == null) "Max Load is required and must be a number" else null
                    volumeError = if (volume.isBlank() || volume.toFloatOrNull() == null) "Volume is required and must be a number" else null

                    val valid = listOf(modelError, plateError, tractorPlateError, maxLoadError, volumeError).all { it == null }

                    if (valid) {
                        isLoading = true
                        viewModel.model = model
                        viewModel.plate = plate
                        viewModel.tractorPlate = tractorPlate
                        viewModel.maxLoad = maxLoad.toFloat()
                        viewModel.volume = volume.toFloat()

                        viewModel.registerVehicle { result ->
                            isLoading = false
                            val message = if (result is Resource.Success && result.data != null) {
                                onVehicleRegistered(result.data)

                                model = ""
                                plate = ""
                                tractorPlate = ""
                                maxLoad = ""
                                volume = ""
                                "Vehicle registered successfully"
                            } else {
                                "Failed to register vehicle"
                            }

                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please complete all fields correctly")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B)),
                enabled = !isLoading
            ) {
                Text("Register Vehicle", color = Color.Black)
            }
        }
    }
}

@Composable
fun InputField(value: String, label: String, onValueChange: (String) -> Unit, error: String?) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
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