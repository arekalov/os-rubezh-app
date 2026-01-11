package com.arekalov.osrubezh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arekalov.osrubezh.presentation.navigation.Navigation
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OsrubezhTheme {
                Navigation()
            }
        }
    }
}
