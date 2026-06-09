package com.shenouda.stride.feature_auth.presentation.splash

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    destination: SplashDestination,
    onResolved: (SplashDestination) -> Unit,
) {
    // Show a centered spinner while the session check is in progress
    Box(
        modifier        = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }

    // Once destination is resolved, notify the graph to navigate
    LaunchedEffect(destination) {
        if (destination !is SplashDestination.Loading) {
            onResolved(destination)
        }
    }
}