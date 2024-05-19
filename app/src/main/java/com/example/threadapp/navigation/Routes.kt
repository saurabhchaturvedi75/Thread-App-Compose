package com.example.threadapp.navigation

sealed class Routes(val route: String) {

    data object Home : Routes("home")
    data object Notification : Routes("notification")
    data object Profile : Routes("profile")
    data object Search : Routes("search")
    data object Splash : Routes("splash")
    data object BottomNavigation : Routes("bottomNavigation")
    data object AddThreads : Routes("addThreads")
    data object Login : Routes("login")
    data object Signup : Routes("signup")
    data object OtherUsers : Routes("other_users/{data}")

}
