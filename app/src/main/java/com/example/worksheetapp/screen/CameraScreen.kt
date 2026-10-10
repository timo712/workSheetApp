package com.example.worksheetapp.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
// this import is to let you be able to take and save pics
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import java.util.UUID


@Composable
fun CameraScreen() {

    // Gives us access to the android app/context
    val context = LocalContext.current

    // CAMERA

    // Check if the user has already allowed camera access

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // This allow us to ask the user for camera permission

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        // Save the user's answer
        hasCameraPermission = isGranted
    }

    // Run this when CameraScreen first opens
    LaunchedEffect(Unit) {

        // ! means Not
        // so : if we dont have camera permission ...
        if (!hasCameraPermission) {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    //---------
    // your existing camerax code
    //---------


    // create the object that control the camera
    // Camerax needs Android's Context to create the camera controller

    val controller = remember {

        //LifecycleCameraConteller  is the brain controlling the camera
        LifecycleCameraController(context).apply {

            // For now, we only want CameraX to take picture

            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    //CAMERA PREVIEW

    //Only show the camera when permission is granted
    // Prevent repeated taps while a photo is being saved

    var isSaving by remember { mutableStateOf(false) }
    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize())
        {
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = {
                    isSaving = true
                    takePhoto(
                        context = context,
                        controller = controller,
                        onFinished = { isSaving = false }
                    )
                },
                enabled = !isSaving,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding()
            ) {
                Text(if (isSaving) "saving.." else "Take photo")
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onFinished: () -> Unit
){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
    {
        Toast.makeText(
            context,
            "This gallery-saving example requires Android 10 or newer.",
            Toast.LENGTH_LONG
        ).show()

        onFinished()
        return
    }

    // Give each phone a  unique filename and a gallery folder

    val photoDetails = ContentValues().apply {
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "Worksheet_${UUID.randomUUID()}.jpg"
        )

        put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
        put(
            MediaStore.Images.Media.RELATIVE_PATH,
            "Pictures/WorksheetApp"
        )
    }

    // telling CameraX where to save the pictures.
    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        photoDetails
    ).build()

    try {
        controller.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        context,
                        "Photo saved to Picture/WorksheetApp",
                        Toast.LENGTH_LONG
                    ).show()
                    onFinished()
                }

                override fun onError(exception: ImageCaptureException)
                {
                    Toast.makeText(
                        context," Could not save photo: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()

                    onFinished()
                }
            }
        )
    } catch (exception: Exception){
        Toast.makeText(
            context,
            "could not take photo: ${exception.message}",
            Toast.LENGTH_LONG
        ).show()
        onFinished()
    }
}
