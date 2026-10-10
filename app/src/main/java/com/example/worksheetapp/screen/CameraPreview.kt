package com.example.worksheetapp.screen

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner


// the CamerPreview is like the Window showing what the phone camera sees
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,  // this the object controlling the camera
    modifier: Modifier = Modifier
){
    val lifecycleOwner = LocalLifecycleOwner.current // tells the CameraX which screen/activity lifecycle it should follow

    AndroidView(
        factory = { context ->

            PreviewView(context).apply {
                this.controller = controller

                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}