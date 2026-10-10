package com.example.worksheetapp.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.worksheetapp.screen.AddWorkSheetScreen
import com.example.worksheetapp.screen.EditWorkSheetScreen
import com.example.worksheetapp.screen.HomeScreen
import com.example.worksheetapp.screen.CameraScreen

// this composable controls navigation between screens

// navController - controls moving between screens
// windowSizeClass - tels you whether the device is mall or large
@Composable
fun AppNavigation
            (navController : NavHostController,
             windowSizeClass: WindowSizeClass
) {
    // this checks if the device is larger than a phone
    val isExpanded = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // Decides which screen should open firsts
    val startDestination = if (isExpanded) "home_detail" else "home"

    //NavHost  is the navigation map open first.
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        // Home screen for compact devices
        composable("home") {
            HomeScreen(
                navController = navController,
                windowSizeClass = windowSizeClass)
        }

        // Home screen for larger device
        composable("home_detail") {
            HomeScreen(
                navController = navController,
                windowSizeClass = windowSizeClass)
        }

        // screen used to create a new worksheet
        composable("add") {
            AddWorkSheetScreen(
                navController = navController
            )
        }

        //Camera screen
        composable("camera"){
            CameraScreen()
        }
        // screen used to edit an existing worksheet
        composable(
            route = "edit/{sheetId}",
            arguments = listOf(navArgument("sheetId") { type = NavType.IntType })
        ){ backStackEntry -> // backStack holds information about current route.

            val sheetId = backStackEntry.arguments?.getInt("sheetId")
            EditWorkSheetScreen(
                sheetId = sheetId, // this gets the file with getInt a
                navController = navController
            )
        }
    }
}