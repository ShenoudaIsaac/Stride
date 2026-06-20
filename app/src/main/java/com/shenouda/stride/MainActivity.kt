package com.shenouda.stride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shenouda.stride.common.core.navigation.AppGraph
import com.shenouda.stride.common.core.navigation.AppNavigation
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.presentation.auth.AuthViewModel
import com.shenouda.stride.feature_auth.presentation.splash.SplashScreen
import com.shenouda.stride.ui.theme.StrideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            StrideTheme {
                AppNavigation(AppGraph.Auth.graph)
            }
        }
    }
}
