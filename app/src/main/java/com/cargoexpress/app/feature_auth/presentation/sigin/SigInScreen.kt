package com.cargoexpress.app.feature_auth.presentation.sigin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    val state = viewModel.state.value

    val username = viewModel.username.value

    val password = viewModel.password.value

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding))
        {
            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.onUsernameChange(it) }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) }
            )
            OutlinedButton(onClick = {
                viewModel.signIn()
            }) {
                Text("Sign In")
            }
            state.user?.let {
                Text("Welcome ${it.username}")
            }
            if (state.error.isNotEmpty()) {
                Text("Usuario o contraseña incorrectos")
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}