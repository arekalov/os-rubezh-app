package com.arekalov.osrubezh.presentation.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Raid : Screen("raid")
    data object Pointers : Screen("pointers")
    data object Numa : Screen("numa")
    data object Disk : Screen("disk")
}
