package com.shenouda.stride.common.core.navigation

sealed class AppGraph(val graph : String) {
    object Auth : AppGraph("auth_graph")
    object Teacher : AppGraph("teacher_graph")
    object Student: AppGraph("student_graph")
}
sealed class AuthRoute(val route: String) {
    object Splash : AuthRoute("splash_route")
    object SignUp : AuthRoute("signup_route")
    object Login : AuthRoute("login_route")
    object RoleSelection: AuthRoute("role_selection_route")
}
sealed class TeacherRoute(val route: String){
    object Home          : TeacherRoute("teacher_home")
    object Upload        : TeacherRoute("teacher_upload")
    object Codes         : TeacherRoute("teacher_codes")
    object Profile       : TeacherRoute("teacher_profile")
    object ContentDetails: TeacherRoute("content_details/{contentId}"){
        fun createRoute(id: String) = "content_details/$id"
    }
    object StudentList    : TeacherRoute("student_list")
    object CodeGenerator  : TeacherRoute("code_generator")
    object EarningsDetail : TeacherRoute("earnings_detail")
}

sealed class StudentRoute(val route: String) {
    object Home : StudentRoute("student_home")
    object Courses : StudentRoute("courses")
    object Homework : StudentRoute("homework")
    object Profile : StudentRoute("student_profile")
    object CourseDetails: StudentRoute("course_details/{courseId}"){
        fun createRoute(id: String) = "course_details/$id"
    }
    object VideoPlayer: StudentRoute("video_player/{videoId}"){
        fun createRoute(id:String)="video_player/$id"
    }
    object CodeUnlock : StudentRoute("code_unlock")
    object HwUpload: StudentRoute("hw_upload/{hwId}"){
        fun createRoute(id: String)="hw_upload/$id"
    }

}