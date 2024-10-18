package com.cargoexpress.app.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cargoexpress.app.core.common.Routes
import com.cargoexpress.app.core.data.remote.login.LoginService
import com.cargoexpress.app.core.data.remote.register.RegisterService
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


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CargoexpressTheme {
                val navController = rememberNavController()

                val loginViewModel = LoginViewModel(navController,LoginRepository(loginService),EntrepreneurRepository(entrepreneurService))
                val registerViewModel = RegisterViewModel(navController,RegisterRepository(registerService),LoginRepository(loginService),
                    ClientRepository(clientService),
                    EntrepreneurRepository(entrepreneurService)
                )
                val vehicleListViewModel = VehicleListViewModel(navController, VehicleRepository(vehicleService),EntrepreneurRepository(entrepreneurService))


                NavHost(navController = navController, startDestination = Routes.Login.routes ){
                    composable(route = Routes.VehicleList.routes){
                        VehicleListScreen(viewModel = vehicleListViewModel)
                    }

                    composable(route = Routes.Login.routes){
                        LoginScreen(viewModel = loginViewModel,navController)
                    }

                    composable(route = Routes.Register.routes){
                        RegisterScreen(navController, viewModel = registerViewModel)
                    }

                }
            }
        }
    }
}

