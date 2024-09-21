package com.cargoexpress.app.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cargoexpress.app.core.ui.theme.CargoexpressTheme
import com.cargoexpress.app.feature_auth.common.Constants
import com.cargoexpress.app.feature_auth.data.remote.AuthService
import com.cargoexpress.app.feature_auth.data.repository.AuthRepository
import com.cargoexpress.app.feature_auth.presentation.sigin.SignInScreen
import com.cargoexpress.app.feature_auth.presentation.sigin.SignInViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val service = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build().create(AuthService::class.java)
    private val viewModel = SignInViewModel(AuthRepository(service))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CargoexpressTheme {
                SignInScreen(viewModel)
            }
        }
    }
}
