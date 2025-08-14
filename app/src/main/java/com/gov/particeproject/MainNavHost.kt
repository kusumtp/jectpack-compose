package com.gov.particeproject

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gov.particeproject.camera.ScannerScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("scanner") {
            ScannerScreen(
                onQrConfirmed = { result ->
                    Log.d("Scanner", "User confirmed QR: $result")
                    // Handle QR result: navigate, store, etc.
                }
            )
        }
    }
}


@Composable
fun Home(navController: NavHostController) {
    var permissionsGranted by remember { mutableStateOf(false) }
    RequestPermissions { granted -> permissionsGranted = granted }
    if (permissionsGranted) {
        // Proceed with your login screen UI
        Surface(modifier = Modifier.fillMaxSize()) {
            // Your login screen UI components
            Text("Permissions granted. Proceed with login.")
        }
    } else {
        // Show a message or handle the case where permissions are not granted
        Text("Permissions are required to proceed.")
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { navController.navigate("scanner") }) {
            Text("Start QR Scan")
        }
    }
}

@Composable
fun RequestPermissions(
    onPermissionsResult: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET
    )
    var allPermissionsGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        allPermissionsGranted = permissions.values.all { it }
        onPermissionsResult(allPermissionsGranted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions)
    }
}
