package com.example.worksheetapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worksheetapp.viewmodel.WorkSheetViewModel
import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView


private val Navy = Color(0xFF14243d)
private val Teal = Color(0xFF117F75)
private val PageBackground = Color(0xFFF3F6FB)
private val BorderColor = Color(0xFFDCE4EF)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkSheetScreen(
    navController: NavController,
    viewModel: WorkSheetViewModel = viewModel()
) {
    // watch the current form values from the ViewModel
    val state by viewModel.addWorkSheetState.collectAsState()

    // Watch whether saving was successful
    val success by viewModel.addWorkSheetSuccess.collectAsState()

    LaunchedEffect(success) {

        if(success) {
            navController.navigate("home"){
                popUpTo("add") {
                    inclusive = true
                }
            }
            viewModel.resetSuccessState()
        }
    }


    Scaffold(

        containerColor =  PageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Worksheet",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription =  "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy
                )
            )
        },
        // Save Button at Bottom
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ){
                Button(
                    onClick = {
                        viewModel.validateAndAddWorkSheet()
                    },
                    enabled =  !state.isSaving && !success,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal
                    )
                ) {
                    Text(
                        if (state.isSaving) "Saving.."
                        else "Save Worksheet",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    ) { paddingValuse ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValuse)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //Worksheet header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Navy
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "worksheet Information",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight =  FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))


                    Text(
                        text = "Enter the repair details below",
                        color = Color(0xFFCED8E6)
                    )
                }
            }

            // Validation Errors

            if ( state.errors.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE8E8)
                    )
                ) {
                    Column( modifier = Modifier.padding(16.dp)
                    ){
                        state.errors.forEach { error ->

                            Text(
                                text = error,
                                color = Color(0xFFB3261E)
                            )
                        }
                    }
                }
            }

            // Section 1: Customer Information

            WorkHubSection("Customer & Work Order")
            {

                WorkHubField(
                    label = "Customer",
                    value = state.customer,
                    onValueChange = {
                        viewModel.updateFormState(customer = it)
                    }
                )


                WorkHubField(
                    label = "Description",
                    value = state.description,
                    onValueChange = {
                        viewModel.updateFormState(description = it)
                    },
                    singleLine = false
                )

                WorkHubField(
                    label = "Work Order",
                    value = state.workOrder,
                    onValueChange = {
                        viewModel.updateFormState(workOrder = it)
                    }
                )
            }

            // Section 2: Cylinder Details

            WorkHubSection("Cylinder Details"){

                WorkHubField(
                    label = "Technician",
                    value = state.tech,
                    onValueChange = {
                        viewModel.updateFormState(tech = it)
                    }
                )

                WorkHubField(
                    label = "Supervisor",
                    value = state.supervisor,
                    onValueChange = {
                        viewModel.updateFormState(supervisor = it)
                    }
                )

                WorkHubField(
                    label = "Tag",
                    value = state.tag,
                    onValueChange = {
                        viewModel.updateFormState(tag = it)
                    }
                )

                WorkHubField(
                    label = "Rod Diameter",
                    value = state.rodDia,
                    onValueChange = {
                        viewModel.updateFormState( rodDia =  it)
                    },
                    numeric = true
                )

                WorkHubField(
                    label = "Rod Length",
                    value = state.rodLgth,
                    onValueChange = {
                        viewModel.updateFormState( rodLgth = it )
                    },
                    numeric = true
                )

                WorkHubField(
                    label = "Rod Condition",
                    value = state.rodCondition,
                    onValueChange = {
                        viewModel.updateFormState(rodCondition = it)
                    }
                )


                WorkHubField(
                    label = "Bore Diameter",
                    value = state.boreDia,
                    onValueChange = {
                        viewModel.updateFormState(boreDia = it)
                    },
                    numeric = true
                )

                WorkHubField(
                    label = "Bore Condition",
                    value = state.boreCondition,
                    onValueChange = {
                        viewModel.updateFormState(boreCondition = it )
                    }
                )
            }


            // Section 3:  Components
            WorkHubSection("Components & Coditions")
            {
                BooleanRow(
                    label = "Gland",
                    checked =  state.gland,
                    onCheckedChange = {
                        viewModel.updateFormState(gland = it)
                    }
                )

                BooleanRow(
                    label = "Eyes",
                    checked = state.eyes,
                    onCheckedChange = {
                        viewModel.updateFormState(eyes = it)
                    }
                )

                BooleanRow(
                    label = "Rod Threads",
                    checked = state.rodThreads,
                    onCheckedChange = {
                        viewModel.updateFormState( rodThreads = it)
                    }
                )

                BooleanRow(
                    label = "Hoses",
                    checked = state.hoses,
                    onCheckedChange = {
                        viewModel.updateFormState( hoses = it)
                    }
                )

                BooleanRow(
                    label = "Pin Wipers",
                    checked = state.pinWipers,
                    onCheckedChange = {
                        viewModel.updateFormState(pinWipers = it)
                    }
                )

                BooleanRow(
                    label = "Fittings",
                    checked = state.fittings,
                    onCheckedChange = {
                        viewModel.updateFormState(fittings =  it)
                    },
                )

                BooleanRow(
                    label = "Machined",
                    checked = state.isMachined,
                    onCheckedChange = {
                        viewModel.updateFormState(isMachined = it)
                    }
                )
            }

            // section 4: Pricing
            WorkHubSection("Parts & Labour")
            {

                WorkHubField(
                    label = "Seal Kit Price",
                    value = state.sealKitPrice,
                    onValueChange = {
                        viewModel.updateFormState(
                            sealKitPrice = it
                        )
                    },
                    numeric = true
                )


                WorkHubField(
                    label = "Washers Price",
                    value = state.washersPrice,
                    onValueChange = {
                        viewModel.updateFormState(
                            washersPrice = it
                        )
                    },
                    numeric = true
                )

                WorkHubField(
                    label ="Fittings Price",
                    value = state.fittingsPrice,
                    onValueChange = {
                        viewModel.updateFormState(
                            fittingsPrice = it
                        )
                    },
                    numeric = true
                )

                WorkHubField(
                    label = "Total Labour",
                    value = state.totalLabour,
                    onValueChange = {
                        viewModel.updateFormState(
                            totalLabour = it
                        )
                    },
                    numeric = true
                )
            }

            // Section 5: Photos

            WorkHubSection("Attached Photos"){

                Text(
                    text = "${state.photoPaths.size} photos attached",
                    color = Color.Gray
                )

                OutlinedButton(
                    onClick = {
                        navController.navigate("camera")
                    },
                    enabled = !state.isSaving && !success,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Teal
                    )
                ) {
                    Text(
                        text = if ( state.photoPaths.isEmpty()){
                            "+ Take Photo"
                        } else {
                            "+ add another Photo"
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }



                // SHOW ALL ATTACHED PHOTOS
                state.photoPaths.forEach { photoPath ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PageBackground
                        )
                    ) {
                        AndroidView(
                            factory = { context->
                                ImageView(context).apply {
                                    scaleType =
                                        ImageView.ScaleType.FIT_CENTER

                                    contentDescription =
                                        "Worksheet attachment"
                                }
                            },

                            update = { imageView ->
                                if (imageView.tag != photoPath) {
                                    imageView.setImageURI(
                                        Uri.parse(photoPath)
                                    )
                                    imageView.tag = photoPath
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            }
        }
    }


@Composable
private  fun WorkHubSection(
    title: String,
    content: @Composable ColumnScope.( ) -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ){
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Navy
            )

            HorizontalDivider(
                color = BorderColor
            )
            content()
        }
    }
}

@Composable
private fun WorkHubField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    numeric: Boolean = false,
    singleLine: Boolean = true
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numeric)
            {
                KeyboardType.Decimal
            } else{
                KeyboardType.Text
            }
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Teal,
            focusedLabelColor = Teal,
            unfocusedBorderColor = BorderColor
        )
    )
}


@Composable
private fun BooleanRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Navy,
            fontWeight = FontWeight.Medium
            )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Teal
            )
        )
    }
}
