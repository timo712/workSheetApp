package com.example.worksheetapp.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worksheetapp.viewmodel.WorkSheetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkSheetScreen(
    navController: NavController,
    sheetId: Int?,
    viewModel: WorkSheetViewModel = viewModel()
){
    // watch all worksheets from the ViewModel
    val worksheets by viewModel.allWorksheet.observeAsState(emptyList())

    if (sheetId == null){
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    val worksheet = worksheets.find{
        it.id == sheetId
    }

    if (worksheet == null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }
    // this how the user can maked edits
    var editedCustomer by remember {
        mutableStateOf(worksheet.customer)
    }

    var editedDescription by remember {
        mutableStateOf( worksheet.description)
    }

    var editedTech by remember {
        mutableStateOf( worksheet.tech)
    }

    var editedSupervisor by remember {
        mutableStateOf( worksheet.supervisor)
    }

    var editedWorkOrder by remember {
        mutableStateOf( worksheet.workOrder)
    }

    var editedTag by remember {
        mutableStateOf(worksheet.tag)
    }

    var editedRodDia by remember {
        mutableStateOf(worksheet.rodDia)
    }

    var editedRodLght by remember {
        mutableStateOf(worksheet.rodLgth)
    }

    var editedRodCondition by remember {
        mutableStateOf(worksheet.rodCondition)
    }

    var editedBoreDia by remember {
        mutableStateOf( worksheet.boreDia)
    }

    var editedBoreCondition by remember {
        mutableStateOf(worksheet.boreCondition)
    }

    // Editable Boolean values.

    var editedGland by remember {
        mutableStateOf(worksheet.gland)
    }

    var editedEye by remember {
        mutableStateOf(worksheet.eyes)
    }

    var editedRodThreads by remember {
        mutableStateOf(worksheet.rodThreads)
    }

    var  editedHoses by remember {
        mutableStateOf(worksheet.hoses)
    }

    var editedPinWipers by remember {
        mutableStateOf(worksheet.pinWipers)
    }

    var editedFittings by remember {
        mutableStateOf(worksheet.fittings)
    }

    var editedIsMachined by remember {
        mutableStateOf(worksheet.isMachined)
    }

    // Prices need to become Strings for TextFields
    var editedSealKitPrice by remember {
        mutableStateOf(worksheet.sealKitPrice.toString())
    }
    var editedWasherPrice by remember {
        mutableStateOf(worksheet.washersPrice.toString())
    }

    var editedFittingsPrice by remember {
        mutableStateOf(worksheet.fittingsPrice.toString())
    }

    var editedTotalLabour by remember {
        mutableStateOf(worksheet.totalLabour.toString())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        OutlinedTextField(
            value = editedCustomer,
            onValueChange = {
                editedCustomer = it
            },
            label = {
                Text( "Customer")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedDescription,
            onValueChange = {
                editedDescription = it
            },
            label = {
                Text( "Description")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedTech,
            onValueChange = {
                editedTech= it
            },
            label = {
                Text( "Technician")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedSupervisor,
            onValueChange = {
                editedSupervisor = it
            },
            label = {
                Text( "Supervisor")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedWorkOrder,
            onValueChange = {
                editedWorkOrder = it
            },
            label = {
                Text("work Order")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedTag,
            onValueChange = {
                editedTag = it
            },
            label = {
                Text("Tag")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedRodDia,
            onValueChange = {
                editedRodDia = it
            },
            label = {
                Text("Rob Diameter")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedRodLght,
            onValueChange = {
                editedRodLght = it
            },
            label = {
                Text("Rod Lenght")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedRodCondition,
            onValueChange = {
                editedRodCondition= it
            },
            label = {
                Text( "Rod Condition")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedBoreDia,
            onValueChange = {
                editedBoreDia = it
            },
            label = {
                Text( "Bore Diameter")
            },
            //this lets you control what kind of keyboard should appear when the field is selected
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal // <- this is meant for decimal number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = editedBoreCondition,
            onValueChange = {
                editedBoreCondition = it
            },
            label = {
                Text( "Bore Condition")
            },
            modifier = Modifier.fillMaxWidth()
        )


        BooleanEditRow(
            label = "Gland",
            checked = editedGland,
            onCheckedChange = {
                editedGland = it
            }
        )

        BooleanEditRow(
            label = "Eyes",
            checked = editedEye,
            onCheckedChange = {
                editedEye = it
            }
        )

        BooleanEditRow(
            label = "Rod Threads",
            checked = editedRodThreads,
            onCheckedChange = {
                editedRodThreads = it
            }
        )

        BooleanEditRow(
            label = "Hoses",
            checked = editedHoses,
            onCheckedChange = {
                editedHoses = it
            }
        )

        BooleanEditRow(
            label = "Pin Wiper",
            checked = editedPinWipers,
            onCheckedChange = {
                editedPinWipers = it
            }
        )

        BooleanEditRow(
            label = "Fittings",
            checked = editedFittings,
            onCheckedChange = {
                editedFittings = it
            }
        )

        BooleanEditRow(
            label = "Machined",
            checked = editedIsMachined,
            onCheckedChange = {
                editedIsMachined = it
            }
        )
        OutlinedTextField(
            value = editedSealKitPrice,
            onValueChange = {
                editedSealKitPrice = it
            },
            label = {
                Text("Seal Kit Price")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = editedWasherPrice,
            onValueChange = {
                editedWasherPrice = it
            },
            label = {
                Text("Washers Price")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = editedFittingsPrice,
            onValueChange = {
                editedFittingsPrice = it
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
            value = editedTotalLabour,
            onValueChange = {
                editedTotalLabour = it
            },
            label = {
                Text("Total Labour")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Delete the existing worksheet
            Button(
                onClick = {
                    viewModel.delete(worksheet)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete worksheet"
                )

                // adds empty space between UI elements
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text("Delete")
            }

            //save the edited values.
            Button(
                onClick = {
                    val updatedWorksheet = worksheet.copy(
                        customer = editedCustomer,
                        description = editedDescription,
                        tech = editedTech,
                        supervisor = editedSupervisor,
                        workOrder = editedWorkOrder,
                        tag = editedTag,

                        rodDia = editedRodDia,
                        rodLgth = editedRodLght,
                        rodCondition = editedRodCondition,
                        boreDia = editedBoreDia,
                        boreCondition = editedBoreCondition,

                        gland = editedGland,
                        eyes = editedEye,
                        rodThreads = editedRodThreads,
                        hoses = editedHoses,
                        pinWipers = editedPinWipers,
                        fittings = editedFittings,

                        sealKitPrice =
                            editedSealKitPrice.toDoubleOrNull() ?: 0.0,

                        washersPrice =
                            editedWasherPrice.toDoubleOrNull() ?: 0.0,

                        fittingsPrice =
                            editedFittingsPrice.toDoubleOrNull() ?: 0.0,

                        totalLabour =
                            editedTotalLabour.toDoubleOrNull() ?: 0.0,

                        isMachined = editedIsMachined
                    )
                    // Send the update worksheet to the ViewModel.
                    viewModel.update(updatedWorksheet)

                    // Go back to the previous screen
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Save Worksheet"
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text("save")
            }
        }
    }
}

@Composable
private fun BooleanEditRow(
    label : String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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