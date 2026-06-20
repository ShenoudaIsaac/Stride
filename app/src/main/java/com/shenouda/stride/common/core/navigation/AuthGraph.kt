package com.shenouda.stride.common.core.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.presentation.auth.AuthViewModel
import com.shenouda.stride.feature_auth.presentation.auth.LoginScreen
import com.shenouda.stride.feature_auth.presentation.auth.RoleSelectionScreen
import com.shenouda.stride.feature_auth.presentation.auth.SignUpScreen
import com.shenouda.stride.feature_auth.presentation.splash.SplashDestination
import com.shenouda.stride.feature_auth.presentation.splash.SplashScreen
import com.shenouda.stride.feature_auth.presentation.splash.SplashViewModel


fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.Auth.graph,
        startDestination = AuthRoute.Splash.route,
    ) {

        // ── Splash ──────────────────────────────────────────────────────────
        composable(AuthRoute.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val destination by viewModel.destination.collectAsStateWithLifecycle()
            SplashScreen(
                destination = destination
            ) { resolved ->
                when (resolved) {
                    is SplashDestination.NewUser ->
                        navController.navigate(AuthRoute.SignUp.route) {
                            popUpTo(AuthRoute.Splash.route) {
                                inclusive = true
                            }
                        }

                    is SplashDestination.NeedsRoleSelection ->
                        navController.navigate(AuthRoute.RoleSelection.route) {
                            popUpTo(AuthRoute.SignUp.route) {
                                inclusive = false
                            }
                        }

                    is SplashDestination.ReturningUser ->
                        if (resolved.role == Role.TEACHER) {
                            navController.navigate(AppGraph.Teacher.graph) {
                                popUpTo(AppGraph.Auth.graph) {
                                    inclusive = true
                                }
                            }
                        } else {
                            navController.navigate(AppGraph.Student.graph) {
                                popUpTo(AppGraph.Auth.graph) {
                                    inclusive = true
                                }
                            }
                        }

                    else -> Unit
                }
            }
        }

        // ── SignUp ───────────────────────────────────────────────────────────
        composable(AuthRoute.SignUp.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val role by viewModel.role.collectAsStateWithLifecycle()
            LaunchedEffect(role) {
                navController.onRoleResolved(role, AuthRoute.SignUp.route)
            }
            SignUpScreen(
                viewModel,
                onLoginClick = {
                    navController.navigate(AuthRoute.Login.route){
                        popUpTo(AuthRoute.Login.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ── Login ────────────────────────────────────────────────────────────
        composable(AuthRoute.Login.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val role by viewModel.role.collectAsStateWithLifecycle()
            LaunchedEffect(role) {
                navController.onRoleResolved(role, AuthRoute.SignUp.route)
            }
            LoginScreen(
                viewModel,
                onSignUpClick = {
                    navController.navigate(AuthRoute.SignUp.route){
                        popUpTo(AuthRoute.Login.route){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthRoute.RoleSelection.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            RoleSelectionScreen(
                viewModel = viewModel,
                onGetStartedButtonClicked = { selectedRole ->
                    when (selectedRole) {
                        Role.TEACHER -> {
                            navController.navigate(AppGraph.Teacher.graph) {
                                popUpTo(AppGraph.Auth.graph) {
                                    inclusive = true
                                }
                            }
                        }

                        else ->
                            navController.navigate(AppGraph.Student.graph) {
                                popUpTo(AppGraph.Auth.graph) {
                                    inclusive = true
                                }
                            }
                    }
                },
                onBackButtonClicked = {
                    viewModel.signOut()
                    navController.navigate(AuthRoute.Login.route) {
                        popUpTo(AuthRoute.RoleSelection.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
private fun NavHostController.onRoleResolved(role: Role?, from: String) {
    when (role) {
        Role.TEACHER -> navigateToHome(AppGraph.Teacher.graph)
        Role.STUDENT -> navigateToHome(AppGraph.Student.graph)
        Role.NONE -> navigate(AuthRoute.RoleSelection.route) {
            popUpTo(from) { inclusive = true }
            launchSingleTop = true
        }
        null -> Unit
    }
}

private fun NavHostController.navigateToHome(graph: String) {
    navigate(graph) {
        popUpTo(AppGraph.Auth.graph) { inclusive = true }
        launchSingleTop = true
    }
}