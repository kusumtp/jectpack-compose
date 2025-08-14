package com.gov.particeproject

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun praticeapp() {
    val navController = rememberNavController() // ✅ Fixed line
    NavHost(
        navController = navController,
        startDestination = "MainScreen"
    ) {
        composable("MainScreen") { MainScreen() }
    }
}
