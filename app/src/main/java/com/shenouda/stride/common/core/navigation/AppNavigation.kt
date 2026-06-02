package com.shenouda.stride.common.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    startGraph: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startGraph) {
        authGraph(navController)
        teacherGraph(navController)
        studentGraph(navController)
    }
}