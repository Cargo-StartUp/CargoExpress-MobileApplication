package com.cargoexpress.app.feature_auth.common

data class UIState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val message: String = ""
)