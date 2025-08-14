package com.gov.particeproject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainScreen() {
    DetailedDrawerExample2 { paddingValues ->
        // This is your main screen content
        val navController = rememberNavController()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // Important: respect the padding from Scaffold
        ) {
            NavHostContainer(navController = navController, padding = paddingValues)
        }
    }

}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        // set the start destination as home
        startDestination = "home",
        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            // route : Home
            composable("home") {
                HomeScreen()
            }
            // route : search
            composable("search") {
                SearchScreen()
            }
            // route : profile
            composable("profile") {
                ProfileScreen()
            }
        }
    )
}
