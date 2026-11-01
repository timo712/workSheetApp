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
import  android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.ui.viewinterop.AndroidView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color

@Composable
fun CameraScreen(
    onUsePhoto: (Uri) -> Unit
) {

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
        // so : if we don't have camera permission ...
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
    // this only displays camera when permission is granted

    // prevents repeated taps while processing.
    var isReading by remember { mutableStateOf(false) }

    // This stores the saved photo's address and update
    var capturedPhotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var recognizedText by remember {
        mutableStateOf<String?>(null)
    }



    val photoUrl = capturedPhotoUri

    if (photoUrl != null){
        // A photo is selected : show it and the Retake button.

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            AndroidView(
                factory = { context ->
                    ImageView(context).apply{
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                },
                update = { imageView ->
                    if( imageView.tag != photoUrl){
                        imageView.setImageURI(photoUrl)
                        imageView.tag = photoUrl
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Row (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Button(
                    onClick = {
                        capturedPhotoUri = null
                        recognizedText = null
                    },
                    enabled = !isReading
                ) {
                    Text("Retake")
                }
                Button(
                    onClick = {
                        onUsePhoto(photoUrl)
                    }
                ) {
                    Text("Use photo")
                }
            }
        }
    } else if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize())
        {
            // the existing composable displays the camera preview
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
                horizontalAlignment =  Alignment.CenterHorizontally
            ){
                IconButton(
                    onClick = {
                        isSaving = true

                        takePhoto(
                            context = context,
                            controller = controller,
                            onPhotoSaved = {
                                uri ->  capturedPhotoUri = uri
                            },
                            onFinished = {
                                isSaving = false
                            }
                        )
                    },
                    enabled = !isSaving,
                    modifier = Modifier
                        .size(82.dp)
                        .border(
                            width = 5.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                        .padding(7.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                ) { // empty for the circle itself

                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Text(
                    text = if (isSaving) "Saving... " else "Take photo",
                    color = Color.White
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text("Camera permission is needed to take photo.")
        }
    }

    val textToShow = recognizedText

    if (textToShow != null){
        AlertDialog(
            onDismissRequest = {
                recognizedText = null
            },
            title = {
                Text("Worksheet text")
            },
            text = {
                Text(
                    text = if (textToShow.isBlank()) {
                        "No text found. Try a clearer photo"
                    } else {
                        textToShow
                    },
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        recognizedText = null
                    }
                ) {
                    Text("close")
                }
            }
        )
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoSaved: (Uri) -> Unit,
    onFinished: () -> Unit

){

    // This implementation saves to the gallery on Android 10 or new
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
                // This would make the photo appear after it was then taken.
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = outputFileResults.savedUri

                    if (savedUri != null){
                        onPhotoSaved(savedUri)
                    } else {
                        Toast.makeText(
                            context,
                            "Photo saved, but its address wasn't returned",
                            Toast.LENGTH_LONG
                        ).show()
                    }

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

private  fun readTextFromPhoto(
    context: Context,
    photoUri: Uri,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
){

    // create a recognizer for Latin-script text, including english

    val recognizer = TextRecognition.getClient(
        TextRecognizerOptions.DEFAULT_OPTIONS
    )

    try {
        // Prepare the save for text
        val image = InputImage.fromFilePath(context,photoUri)

        // process the photo and return the result asynchrouosly
        recognizer.process(image)
            .addOnSuccessListener { result ->
                onSuccess(result.text)
            }

            .addOnFailureListener {  exception ->
                onError(exception)
            }

            .addOnCompleteListener {
                recognizer.close()
            }
    } catch (exception: Exception){
        recognizer.close()
        onError(exception)
    }
}
