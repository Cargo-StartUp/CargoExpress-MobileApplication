package com.cargoexpress.app.feature_auth.presentation.sigin

import com.cargoexpress.app.feature_auth.domain.model.User

data class SignInState(
    val isLoading: Boolean = false,
    val user:  User? = null,
    val error: String = ""
)