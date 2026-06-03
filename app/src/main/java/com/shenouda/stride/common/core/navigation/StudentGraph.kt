package com.shenouda.stride.common.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.studentGraph(navController: NavHostController) {
    navigation(
        route = AppGraph.Student.graph,
        startDestination = StudentRoute.Home.route,
    ) {
        composable(StudentRoute.Home.route) {
            StudentHomeScreen(navController)
        }
        composable(StudentRoute.Courses.route) {
            CoursesScreen(navController)
        }
        composable(StudentRoute.Homework.route) {
            HomeworkScreen(navController)
        }
    }
}
