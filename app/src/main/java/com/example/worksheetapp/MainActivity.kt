package com.example.worksheetapp
// packages are files that belong to this
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.example.worksheetapp.navigation.AppNavigation
import com.example.worksheetapp.ui.theme.WorkSheetAppTheme

// this the first screen android launches.

//So your MainActivity does not need to manually know every button or every screen.
// It starts the Compose app, creates the navigation controller,
// then AppNavigation handles the screen changes.

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            WorkSheetAppTheme {
                // Get window size class for responsive layouts & Calculates the device screen size
                val windowSizesClass = calculateWindowSizeClass(this@MainActivity)
                // manages those transitions
                val navController = rememberNavController()

                AppNavigation(
                    navController = navController,
                    windowSizeClass = windowSizesClass
                )
            }
        }
    }
}



