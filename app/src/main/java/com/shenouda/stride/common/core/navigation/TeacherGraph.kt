package com.shenouda.stride.common.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.teacherGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.Teacher.graph,
        startDestination = TeacherRoute.Home.route
    ){
        composable(TeacherRoute.Home.route) {
            TeacherHomeScreen(navController)
        }
        composable(TeacherRoute.Upload.route) {
            TeacherUploadScreen(navController)
        }
        composable(TeacherRoute.Codes.route) {
            TeacherCodesScreen(navController)
        }
    }
}