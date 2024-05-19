package com.example.threadapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadapp.screens.AddThreads
import com.example.threadapp.screens.BottomNav
import com.example.threadapp.screens.Home
import com.example.threadapp.screens.Login
import com.example.threadapp.screens.Notification
import com.example.threadapp.screens.OtherUsers
import com.example.threadapp.screens.Profile
import com.example.threadapp.screens.Search
import com.example.threadapp.screens.Signup
import com.example.threadapp.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = Routes.Splash.route) {

        composable(Routes.Splash.route) {
            Splash(navController)
        }
        composable(Routes.AddThreads.route) {
            AddThreads(navController)
        }
        composable(Routes.Home.route) {
            Home(navController)
        }
        composable(Routes.Notification.route) {
            Notification()
        }
        composable(Routes.Profile.route) {
            Profile(navController)
        }
        composable(Routes.Search.route) {
            Search(navController)
        }
        composable(Routes.BottomNavigation.route) {
            BottomNav(navController)
        }
        composable(Routes.Login.route) {
            Login(navController)
        }
        composable(Routes.Signup.route) {
           Signup(navController)
        }
        composable(Routes.OtherUsers.route){
            val data  = it.arguments!!.getString("data")
            OtherUsers(navController,data!!)
        }

    }
}