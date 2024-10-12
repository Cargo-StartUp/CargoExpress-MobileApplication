package com.cargoexpress.app.core.common

sealed class Routes(val routes: String) {
    data object VehicleList: Routes("VehicleList")
    data object Login: Routes("Login")
    data object Register: Routes("Register")



}