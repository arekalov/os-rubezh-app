package com.arekalov.osrubezh.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.arekalov.osrubezh.presentation.main.MainScreen
import com.arekalov.osrubezh.presentation.numa.NUMAScreen
import com.arekalov.osrubezh.presentation.pointers.PointersScreen
import com.arekalov.osrubezh.presentation.raid.RAIDScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberSwipeDismissableNavController()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        
        composable(Screen.Raid.route) {
            RAIDScreen()
        }
        
        composable(Screen.Pointers.route) {
            PointersScreen()
        }
        
        composable(Screen.Numa.route) {
            NUMAScreen()
        }
    }
}
