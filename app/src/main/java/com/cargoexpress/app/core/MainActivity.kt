
package com.cargoexpress.app.core

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cargoexpress.app.core.common.Routes
import com.cargoexpress.app.core.data.remote.login.LoginService
import com.cargoexpress.app.core.data.remote.register.RegisterService
import com.cargoexpress.app.core.data.remote.trip.TripService
import com.cargoexpress.app.core.data.remote.user.ClientService
import com.cargoexpress.app.core.data.remote.user.EntrepreneurService
import com.cargoexpress.app.core.data.remote.vehicle.VehicleService
import com.cargoexpress.app.core.data.repository.ClientRepository
import com.cargoexpress.app.core.data.repository.EntrepreneurRepository
import com.cargoexpress.app.core.data.repository.LoginRepository
import com.cargoexpress.app.core.data.repository.RegisterRepository
import com.cargoexpress.app.core.data.repository.VehicleRepository
import com.cargoexpress.app.core.presentation.vehicle.VehicleListViewModel
import com.cargoexpress.app.core.presentation.login.LoginScreen
import com.cargoexpress.app.core.presentation.login.LoginViewModel
import com.cargoexpress.app.core.presentation.register.RegisterScreen
import com.cargoexpress.app.core.presentation.register.RegisterViewModel
import com.cargoexpress.app.core.presentation.vehicle.VehicleListScreen
import com.cargoexpress.app.core.ui.theme.CargoexpressTheme
import pe.edu.upc.appturismo.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.DirectionsBusFilled
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.twotone.AppRegistration
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val loginService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginService::class.java)

        val registerService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegisterService::class.java)

        val clientService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientService::class.java)

        val entrepreneurService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EntrepreneurService::class.java)

        val vehicleService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleService::class.java)

        val tripService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TripService::class.java)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CargoexpressTheme {

                val navController = rememberNavController()

                val loginViewModel = LoginViewModel(navController, LoginRepository(loginService))
                val registerViewModel = RegisterViewModel(
                    navController,
                    RegisterRepository(registerService),
                    LoginRepository(loginService),
                    ClientRepository(clientService),
                    EntrepreneurRepository(entrepreneurService)
                )
                val vehicleListViewModel =
                    VehicleListViewModel(navController, VehicleRepository(vehicleService))

                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentDestination == Routes.Register.routes,
                                onClick = { navController.navigate(Routes.Register.routes) },
                                icon = {
                                  Icon(imageVector = Icons.TwoTone.AppRegistration, contentDescription = "Registro")

                                },
                                label = { Text("Registro") }
                            )
                            NavigationBarItem(
                                selected = currentDestination == Routes.VehicleList.routes,
                                onClick = { navController.navigate(Routes.VehicleList.routes) },
                                icon = { Icon(Icons.Filled.DirectionsBusFilled, contentDescription = "Flota") },
                                label = { Text("Flota") }
                            )
                            NavigationBarItem(
                                selected = currentDestination == "history",
                                onClick = {
                                },
                                icon = { Icon(Icons.Filled.Autorenew, contentDescription = "Flota") },

                                label = { Text("Historial") }
                            )
                            NavigationBarItem(
                                selected = currentDestination == "gps",
                                onClick = {
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = "GPS"
                                    )
                                },
                                label = { Text("GPS") }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Login.routes,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Routes.VehicleList.routes) {
                            VehicleListScreen(viewModel = vehicleListViewModel)
                        }
                        composable(route = Routes.Login.routes) {
                            LoginScreen(viewModel = loginViewModel, navController)
                        }
                        composable(route = Routes.Register.routes) {
                            RegisterScreen(navController, viewModel = registerViewModel)
                        }
                        composable(route = "history") {

                        }
                        composable(route = "gps") {

                        }
                    }
                }
            }
        }
    }
}