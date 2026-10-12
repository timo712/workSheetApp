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
import androidx.compose.ui.viewinterop.AndroidView

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Worksheet")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription =  "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValuse ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValuse)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "worksheet Information",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display validation errors
                    state.errors.forEach { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    OutlinedTextField(
                        value = state.customer,
                        onValueChange = {
                            viewModel.updateFormState(customer = it)
                        },
                        label = {
                            Text("Customer")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.description,
                        onValueChange = {
                            viewModel.updateFormState(description = it)
                        },
                        label = {
                            Text("Description")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.tech,
                        onValueChange = {
                            viewModel.updateFormState(tech = it)
                        },
                        label = {
                            Text("Technician ")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.supervisor,
                        onValueChange = {
                            viewModel.updateFormState(supervisor = it)
                        },
                        label = {
                            Text("Supervisor")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.workOrder,
                        onValueChange = {
                            viewModel.updateFormState(workOrder = it)
                        },
                        label = {
                            Text("work Order")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.tag,
                        onValueChange = {
                            viewModel.updateFormState(tag = it)
                        },
                        label = {
                            Text("Tag")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.rodDia,
                        onValueChange = {
                            viewModel.updateFormState(rodDia = it)
                        },
                        label = {
                            Text("Rod Diameter")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    OutlinedTextField(
                        value = state.rodLgth,
                        onValueChange = {
                            viewModel.updateFormState(rodLgth = it)
                        },
                        label = {
                            Text("Rod Length")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.rodCondition,
                        onValueChange = {
                            viewModel.updateFormState(rodCondition = it)
                        },
                        label = {
                            Text("Rod Condition")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.boreDia,
                        onValueChange = {
                            viewModel.updateFormState(boreDia = it)
                        },
                        label = {
                            Text("Bore Dia")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.boreCondition,
                        onValueChange = {
                            viewModel.updateFormState(boreCondition = it)
                        },
                        label = {
                            Text("Bore Condition")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BooleanRow(
                        label = "Gland",
                        checked = state.gland,
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
                        label = "Rod Threads ",
                        checked = state.rodThreads,
                        onCheckedChange = {
                            viewModel.updateFormState(rodThreads = it)
                        }
                    )

                    BooleanRow(
                        label = "Hoses",
                        checked = state.hoses,
                        onCheckedChange = {
                            viewModel.updateFormState(hoses = it)
                        }
                    )

                    BooleanRow(
                        label = "Pin Wiper",
                        checked = state.pinWipers,
                        onCheckedChange = {
                            viewModel.updateFormState(pinWipers = it)
                        }
                    )
                    BooleanRow(
                        label = "Fittings",
                        checked = state.fittings,
                        onCheckedChange = {
                            viewModel.updateFormState(fittings = it )
                        }
                    )

                    BooleanRow(
                        label = "Machined",
                        checked = state.isMachined,
                        onCheckedChange = {
                            viewModel.updateFormState(isMachined = it)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.sealKitPrice,
                        onValueChange = {
                            viewModel.updateFormState(
                                sealKitPrice = it
                            )
                        },
                        label = {
                            Text("Seal Kit Price ")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.washersPrice,
                        onValueChange = {
                            viewModel.updateFormState(
                                washersPrice = it
                            )
                        },
                        label = {
                            Text("wisher Price")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.fittingsPrice,
                        onValueChange = {
                            viewModel.updateFormState(
                                fittingsPrice = it
                            )
                        },
                        label = {
                            Text("Fittings Price")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.totalLabour,
                        onValueChange = {
                            viewModel.updateFormState(
                                totalLabour = it
                            )
                        },
                        label = {
                            Text("Total Labour")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                           navController.navigate("camera")
                        },
                        enabled = !state.isSaving && !success,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.photoPaths.isEmpty())
                        {"Take photo"

                           } else {
                               "add another photo"
                        }

                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // show how many photos are attached.
                    Text("${state.photoPaths.size} photos attached")

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.validateAndAddWorkSheet()
                        },
                        enabled = !state.isSaving &&
                            !success,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (state.isSaving) {
                                "saving"
                            } else {
                                "save Worksheet "
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(success) {
        if (success) {
            navController.navigate("home"){
                popUpTo("add"){
                    inclusive = true
                }
            }
            viewModel.resetSuccessState()
        }
    }
}

@Composable
private fun BooleanRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(label)

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
