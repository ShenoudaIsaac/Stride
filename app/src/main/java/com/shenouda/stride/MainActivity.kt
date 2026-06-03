package com.shenouda.stride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep splash visible until your app is ready
        splashScreen.setKeepOnScreenCondition {
            !viewModel.isReady.value   // e.g. waiting for data/auth check
        }
        setContent {
            StrideTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val role by authViewModel.role.collectAsStateWithLifecycle()

                when (role) {
                    null         ->   SplashLoadingScreen()  // DataStore still loading
                    Role.NONE    -> AppNavigation(AppGraph.Auth.graph)
                    Role.TEACHER -> AppNavigation(AppGraph.Teacher.graph)
                    Role.STUDENT -> AppNavigation(AppGraph.Student.graph)
                }
            }
        }
    }
}
