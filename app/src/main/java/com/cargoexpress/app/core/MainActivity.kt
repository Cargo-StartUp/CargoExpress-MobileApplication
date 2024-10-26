package com.cargoexpress.app.core

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.twotone.AppRegistration
import androidx.compose.material.icons.twotone.LocalShipping
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cargoexpress.app.R
import com.cargoexpress.app.core.data.remote.driver.DriverService
import com.cargoexpress.app.core.data.remote.expense.ExpenseService
import com.cargoexpress.app.core.data.repository.DriverRepository
import com.cargoexpress.app.core.data.repository.ExpenseRepository
import com.cargoexpress.app.core.presentation.driver.driverList.DriverListScreen
import com.cargoexpress.app.core.presentation.driver.driverList.DriverListViewModel
import com.cargoexpress.app.core.presentation.driver.driverList.registerDriver.RegisterDriverScreen
import com.cargoexpress.app.core.presentation.driver.driverList.registerDriver.RegisterDriverViewModel
//import com.cargoexpress.app.core.presentation.record.registerExpense.RegisterExpenseScreen
import com.cargoexpress.app.core.presentation.trip.registerTrip.RegisterTripScreen
import com.cargoexpress.app.core.presentation.trip.registerTrip.RegisterTripViewModel
import com.cargoexpress.app.core.presentation.vehicle.registerVehicle.RegisterVehicleScreen
import com.cargoexpress.app.core.presentation.vehicle.registerVehicle.RegisterVehicleViewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
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

        val expenseService = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExpenseService::class.java)

        val tripRepository = TripRepository(tripService)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CargoexpressTheme {
                val navController = rememberNavController()
                val loginViewModel = LoginViewModel(navController, LoginRepository(loginService), EntrepreneurRepository(entrepreneurService))
                val registerViewModel = RegisterViewModel(
                    navController,
                    RegisterRepository(registerService),
                    LoginRepository(loginService),
                    ClientRepository(clientService),
                    EntrepreneurRepository(entrepreneurService)
                )
                val vehicleListViewModel = VehicleListViewModel(navController, VehicleRepository(vehicleService), EntrepreneurRepository(entrepreneurService))
                //driver
                val driverRepository = DriverRepository(driverService)
                val driverListViewModel = DriverListViewModel(navController, driverRepository)

                //expense
                val expenseRepository = ExpenseRepository(expenseService)

                //vehicle
                val vehicleRepository = VehicleRepository(vehicleService)

                //Trip
                val tripRepository = TripRepository(tripService)
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                val currentRoute = navController.currentBackStackEntry?.destination?.route

                @Composable
                fun MyAppBar(onProfileClick: () -> Unit) {
                    TopAppBar(
                        title = { Text("CargoExpress") },
                        navigationIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(40.dp)
                            )
                        },// Título de la AppBar
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
                                    selected = currentDestination == "trips",
                                    onClick = { navController.navigate("trips") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.TwoTone.LocalShipping,
                                            contentDescription = "Mis Viajes"
                                        )
                                    },
                                    label = { Text("Mis Viajes") }
                                )
                                NavigationBarItem(
                                    selected = currentDestination == "vehicles",
                                    onClick = { navController.navigate("vehicles") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.DirectionsBusFilled,
                                            contentDescription = "Mis Vehiculos"
                                        )
                                    },
                                    label = { Text("Mis Vehiculos") }
                                )
                                NavigationBarItem(
                                    selected = currentDestination == "drivers",
                                    onClick = { navController.navigate("drivers") },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Groups,
                                            contentDescription = "Mis Conductores"
                                        )
                                    },
                                    label = { Text("Mis Conductores") }
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
                        // Login And Register
                        composable(route = Routes.Login.routes) {
                            LoginScreen(viewModel = loginViewModel, navController)
                        }
                        composable(route = Routes.Register.routes) {
                            RegisterScreen(navController, viewModel = registerViewModel)
                        }

                        composable(route = Routes.TripList.routes) {
                            TripManagementScreen(tripRepository = tripRepository, navController)
                        }
                        composable(route = "trips") {
                            TripManagementScreen(tripRepository = tripRepository, navController)
                        }
                        composable(route = "vehicles") {
                            VehicleListScreen(viewModel = vehicleListViewModel, navController)
                            // FleetScreen(navController)
                        }
                        composable(route = "drivers") {
                            DriverListScreen(viewModel = driverListViewModel, navController)
                            // TripManagementScreen(tripRepository = tripRepository)
                        }
                        composable(route = "gps") {
                            // GPS screen
                        }


                       /*composable(
                           route = "register_expense") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            val registerExpenseViewModel = RegisterExpenseViewModel(expenseRepository)
                            RegisterExpenseScreen(viewModel = registerExpenseViewModel) { expense ->
                                // Handle driver registration success
                            }
                        }
*/
                        composable(route = "register_driver") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            val registerDriverViewModel = RegisterDriverViewModel(driverRepository)
                            RegisterDriverScreen(viewModel = registerDriverViewModel, navController = navController) { driver ->

                            }
                        }

                        composable(route = "register_vehicle") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            val registerVehicleViewModel = RegisterVehicleViewModel(vehicleRepository)
                            RegisterVehicleScreen(viewModel = registerVehicleViewModel, navController = navController) { vehicle ->
                                // Handle vehicle registration success
                            }
                        }

                        composable(route = "register_trip") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            val registerTripViewModel = RegisterTripViewModel(tripRepository)
                            RegisterTripScreen (viewModel = registerTripViewModel) { trip ->
                                // Handle trip registration success
                            }
                        }
                    }
                }
            }
        }
    }
}