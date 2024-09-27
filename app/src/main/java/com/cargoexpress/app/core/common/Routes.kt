package com.cargoexpress.app.core.common

sealed class Routes(val routes: String) {
    data object VehicleList: Routes("VehicleList")

}