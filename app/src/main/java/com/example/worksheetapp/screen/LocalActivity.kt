package com.example.worksheetapp.screen

import android.app.Activity
import androidx.compose.runtime.compositionLocalOf

val LocalActivity = compositionLocalOf<Activity>{
    error("LocalActivity not provided")
}