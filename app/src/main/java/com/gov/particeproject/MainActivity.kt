package com.gov.particeproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.gov.particeproject.MyCafe.MyCafe
import com.gov.particeproject.ui.theme.ParticeProjectTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParticeProjectTheme {
//                praticeapp()
//                MainNavHost()
                MyCafe()
            }
        }
    }
}

