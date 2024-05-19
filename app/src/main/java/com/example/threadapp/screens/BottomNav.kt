package com.example.threadapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.threadapp.model.BottomNavItem
import com.example.threadapp.navigation.Routes

@Composable
fun BottomNav(navController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar(navController1) }) { padding ->
        NavHost(
            navController = navController1, startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.AddThreads.route) {
                AddThreads(navController1)
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

        }

    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    BottomNav(rememberNavController())
}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "Home",
            Routes.Home.route,
            Icons.Default.Home,
            modifier = Modifier
                .size(20.dp)

        ),
        BottomNavItem(
            "Search",
            Routes.Search.route,
            Icons.Default.Search,
            modifier = Modifier
                .size(20.dp)


        ),
        BottomNavItem(
            "Add Threads",
            Routes.AddThreads.route,
            Icons.Default.AddCircle,
            modifier = Modifier
                .size(40.dp)


        ),
        BottomNavItem(
            "Notification",
            Routes.Notification.route,
            Icons.Default.Notifications,
            modifier = Modifier
                .size(20.dp)


        ),
        BottomNavItem(
            "Profile",
            Routes.Profile.route,
            Icons.Default.Person,
            modifier = Modifier
                .size(20.dp)


        )
        
    )

    BottomAppBar( ) {
        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route

            NavigationBarItem(selected = selected, onClick = {
                navController1.navigate(it.route) {
                    popUpTo(navController1.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }, icon = {
                Icon(imageVector = it.icon, contentDescription = it.title, modifier = it.modifier)
            }
            )
        }
    }
}