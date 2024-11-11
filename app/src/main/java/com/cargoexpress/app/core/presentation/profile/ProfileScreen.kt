package com.cargoexpress.app.core.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.appturismo.common.Constants

@Composable
fun UserProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val userProfile = viewModel.userProfile.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile(userId = Constants.USER_ID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        userProfile?.let { profile ->
            Text(
                text = "Name: ${profile.name}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${profile.email}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Phone: ${profile.phone}")
        } ?: run {
            CircularProgressIndicator()
        }
    }
}