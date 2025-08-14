package com.gov.particeproject.Grocery

sealed class SealedClassScreen(val route: String) {
    object Splash : SealedClassScreen("splash")
    object Login : SealedClassScreen("login")
    object Signup : SealedClassScreen("signup")
    object Home : SealedClassScreen("home")
    object ProductDetails : SealedClassScreen("productDetails")
    object Cart : SealedClassScreen("cart")
    object Profile : SealedClassScreen("profile")
}

