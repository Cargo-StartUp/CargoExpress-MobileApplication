package com.cargoexpress.app.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
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
import com.cargoexpress.app.core.data.repository.TripRepository
import com.cargoexpress.app.core.data.repository.VehicleRepository
import com.cargoexpress.app.core.presentation.vehicle.VehicleListViewModel
import com.cargoexpress.app.core.presentation.login.LoginScreen
import com.cargoexpress.app.core.presentation.login.LoginViewModel
import com.cargoexpress.app.core.presentation.register.RegisterScreen
import com.cargoexpress.app.core.presentation.register.RegisterViewModel
import com.cargoexpress.app.core.presentation.vehicle.VehicleListScreen
import com.cargoexpress.app.core.presentation.record.RecordScreen
import com.cargoexpress.app.core.presentation.fleet.FleetScreen
import com.cargoexpress.app.core.presentation.trip.TripManagementScreen
import com.cargoexpress.app.core.ui.theme.CargoexpressTheme
import pe.edu.upc.appturismo.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.DirectionsBusFilled
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.twotone.AppRegistration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cargoexpress.app.core.data.remote.driver.DriverService
import com.cargoexpress.app.core.data.repository.DriverRepository
import com.cargoexpress.app.core.presentation.driver.driverList.DriverListScreen
import com.cargoexpress.app.core.presentation.driver.driverList.DriverListViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
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

        val driverService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DriverService::class.java)

        val tripRepository = TripRepository(tripService)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CargoexpressTheme {
                val navController = rememberNavController()
                val loginViewModel = LoginViewModel(navController,LoginRepository(loginService),EntrepreneurRepository(entrepreneurService))
                val registerViewModel = RegisterViewModel(
                    navController,
                    RegisterRepository(registerService),
                    LoginRepository(loginService),
                    ClientRepository(clientService),
                    EntrepreneurRepository(entrepreneurService)
                )
                val vehicleListViewModel = VehicleListViewModel(navController, VehicleRepository(vehicleService),EntrepreneurRepository(entrepreneurService))

                val driverRepository = DriverRepository(driverService)
                val driverListViewModel = DriverListViewModel(navController, driverRepository)
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                val currentRoute = navController.currentBackStackEntry?.destination?.route

                @Composable
                fun MyAppBar(onProfileClick: () -> Unit) {
                    TopAppBar(
                        title = { Text("CargoExpress") }, // Título de la AppBar
                        actions = {
                            IconButton(onClick = { onProfileClick() }) {
                                Icon(imageVector = Icons.Filled.AccountCircle, modifier = Modifier.size(100.dp), contentDescription = "Perfil")
                            }
                        }
                    )
                }
                Scaffold(
                    topBar = {
                        if (currentRoute != Routes.Login.routes && currentRoute != Routes.Register.routes) {
                            MyAppBar(onProfileClick = {
                                // Navegar a la pantalla de perfil
                                navController.navigate("profile")
                            })
                        }
                    },
                    bottomBar = {
                        if (currentRoute != Routes.Login.routes && currentRoute != Routes.Register.routes) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentDestination == "record",
                                    onClick = { navController.navigate("record") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.TwoTone.AppRegistration,
                                            contentDescription = "Registro"
                                        )
                                    },
                                    label = { Text("Registro") }
                                )
                                NavigationBarItem(
                                    selected = currentDestination == "fleet",
                                    onClick = { navController.navigate("fleet") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.DirectionsBusFilled,
                                            contentDescription = "Flota"
                                        )
                                    },
                                    label = { Text("Flota") }
                                )
                                NavigationBarItem(
                                    selected = currentDestination == "trip",
                                    onClick = { navController.navigate("trip") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Autorenew,
                                            contentDescription = "Historial"
                                        )
                                    },
                                    label = { Text("Historial") }
                                )
                                NavigationBarItem(
                                    selected = currentDestination == "gps",
                                    onClick = { },
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
                        composable (route = Routes.Record.routes ){
                            RecordScreen()
                        }
                        composable(route = "record") {
                            RecordScreen()
                        }
                        composable(route = "fleet") {
                            FleetScreen(navController)
                        }
                        composable(route = "trip") {
                            TripManagementScreen(tripRepository = tripRepository)
                        }
                        composable(route = Routes.DriverList.routes) {
                            DriverListScreen(viewModel = driverListViewModel)
                        }
                        composable(route = "gps") {
                            // GPS screen
                        }
                    }
                }
            }
        }
    }
}
