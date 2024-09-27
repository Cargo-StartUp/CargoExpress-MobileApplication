package com.cargoexpress.app.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cargoexpress.app.core.common.Routes
import com.cargoexpress.app.core.data.remote.VehicleService
import com.cargoexpress.app.core.data.repository.VehicleRepository
import com.cargoexpress.app.core.presentation.VehicleListScreen
import com.cargoexpress.app.core.presentation.VehicleListViewModel
import com.cargoexpress.app.core.ui.theme.CargoexpressTheme
import pe.edu.upc.appturismo.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val vehicleService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleService::class.java)

        super.onCreate(savedInstanceState)

        // Configurar OkHttpClient con el Interceptor para añadir el token en cada solicitud


        enableEdgeToEdge()
        setContent {
            CargoexpressTheme {
                val navController = rememberNavController()

                val vehicleListViewModel = VehicleListViewModel(navController, VehicleRepository(vehicleService))
                NavHost(navController = navController, startDestination = Routes.VehicleList.routes ){
                    composable(route = Routes.VehicleList.routes){
                        VehicleListScreen(viewModel = vehicleListViewModel)
                    }
                }
            }
        }
    }
}

