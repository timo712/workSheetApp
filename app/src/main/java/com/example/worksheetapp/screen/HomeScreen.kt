package com.example.worksheetapp.screen


import android.R.attr.value
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worksheetapp.data.Worksheet
import com.example.worksheetapp.viewmodel.WorkSheetViewModel


@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun HomeScreen(
    navController: NavController, // giving then home the ability to move around the app.
    windowSizeClass: WindowSizeClass,
    viewModel: WorkSheetViewModel = viewModel()
) {
    // Watch the worksheet list from the viewModel, start with an empty list while loading, and automatically
    // when Room changes the worksheet list :
    // Room -> LiveData -> Compose state -> screen redraws

    // update the UI whenever the worksheet data changes
    val worksheets by viewModel.allWorksheet.observeAsState(emptyList())

    // check whether the app is using a larger window
    val isExpandedScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // Remember the worksheet selected on a larger screen
    var selectedWorkSheet by remember {
        mutableStateOf<Worksheet?>(null)
    }

    // Text current entered using their work-order number
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    // Filter worksheets using their work-order number.
    val filteredWorksheets = remember(
        worksheets,
        searchQuery
    ) {
        val query = searchQuery.trim()

        if (query.isEmpty()) {
            worksheets
        } else {
            worksheets.filter { worksheets ->
                worksheets.workOrder.contains(
                    other = query,
                    ignoreCase = true
                )
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Worksheet List")
                },
                actions = {
                    // Open the Add Worksheer screen.
                    IconButton(
                        onClick = {
                            navController.navigate("add")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add worksheet"
                        )
                    }
                }
            )
        }

        // giving padding makes sure that content doesn't overlap the top bar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                label = {
                    Text("Search by work order")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 16.dp,
                        bottom = 12.dp
                    )
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(top = 8.dp)
            )

            //  this if is to check what device its on
            if (isExpandedScreen) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    WorksheetList(
                        worksheets = filteredWorksheets,

                        emptyMessage = if (
                            searchQuery.isBlank()
                        ) {
                            "No worksheets yet. tap + tp add one"
                        } else {
                            "no work order matches \"$searchQuery\"."
                        },

                        selectedWorksheet = selectedWorkSheet,
                        onSelect = { worksheet ->
                            selectedWorkSheet = worksheet
                        },
                        onEdit = { worksheet ->
                            navController.navigate(
                                "edit/${worksheet.id}"
                            )
                        },
                        onDelete = { worksheet ->
                            viewModel.delete(worksheet)

                            if (
                                selectedWorkSheet?.id ==
                                worksheet.id
                            ) {
                                selectedWorkSheet = null
                            }
                        },

                        onToggleMachined = { worksheet ->
                            viewModel.toggleMachined(worksheet)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    WorksheetDetails(
                        worksheet = selectedWorkSheet,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(16.dp)
                    )
                }
            } else {
                // Phone layout
                WorksheetList(
                    worksheets = filteredWorksheets,

                    emptyMessage = if (
                        searchQuery.isBlank()
                    ) {
                        "No worksheets yet. tap + to add one"
                    } else {
                        "No work oder matches \"$searchQuery\"."
                    },
                    selectedWorksheet = null,
                    onSelect = {},
                    onEdit = { worksheet ->
                        navController.navigate(
                            "edit/${worksheet.id}"
                        )
                    },
                    onDelete = { worksheet ->
                        viewModel.delete(worksheet)
                    },
                    onToggleMachined = { worksheet ->
                        viewModel.toggleMachined(worksheet)
                    },

                    // fill the remaining area under the search bar
                    modifier = Modifier.padding()
                )
            }
        }
    }
}
@Composable
private fun WorksheetList (
    worksheets: List<Worksheet>,
    emptyMessage: String,
    selectedWorksheet: Worksheet?,
    onSelect: (Worksheet) -> Unit,
    onEdit: (Worksheet) -> Unit,
    onDelete: (Worksheet) -> Unit,
    onToggleMachined: (Worksheet) -> Unit,
    modifier: Modifier = Modifier
){
    if(worksheets.isEmpty()){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = worksheets,
            key = { worksheet ->
                worksheet.id
            }
        ){ worksheet ->
            WorksheetItem(
                worksheet = worksheet,
                onEdit = {
                    onEdit(worksheet)
                },
                onDelete = {
                    onDelete(worksheet)
                },
                onToggleMachined = {
                    onToggleMachined(worksheet)
                },
                modifier = Modifier
                    .clickable {
                        onSelect(worksheet)
                    }
                    .background(
                        if (worksheet == selectedWorksheet){
                            MaterialTheme.colorScheme.secondaryContainer
                        }else{
                            Color.Transparent
                        }
                    )
            )
        }
    }
}

@Composable
private fun WorksheetItem(
    worksheet: Worksheet,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleMachined: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Text(
            text = worksheet.customer,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Work Order: ${worksheet.workOrder}"
        )
        Text(
            text = "Technician: ${worksheet.tech}"
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            // Change the worksheet's machined status.
            IconButton(
                onClick = onToggleMachined
            ) {
                Icon(
                    imageVector = if (worksheet.isMachined){
                        Icons.Default.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = "Toggle machined"
                )
            }

            // Open the Edit Worksheet screen.
            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit worksheet"
                )
            }

            // Delete the worksheet.
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete worksheet"
                )
            }
        }
    }
}

@Composable
private  fun WorksheetDetails(
    worksheet: Worksheet?,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ){
        worksheet?.let { sheet  ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "Worksheet",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                FormRow(
                    label = "Customer",
                    value = sheet.customer
                )

                FormRow(
                    label = "Description",
                    value = sheet.description
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FormCell(
                        label = "Technician",
                        value = sheet.tech,
                        modifier = Modifier.weight(1f)
                    )

                    FormCell(
                        label = "Supervisor",
                        value = sheet.supervisor,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FormCell(
                        label = "Work order",
                        value = sheet.workOrder,
                        modifier = Modifier.weight(1f)
                    )

                    FormCell(
                        label = "Tag",
                        value = sheet.tag,
                        modifier = Modifier.weight(1f)
                    )
                }

                FormSection(
                    title = "MEASUREMENTS"
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    FormCell(
                        label = "Rod diameter",
                        value = sheet.rodDia,
                        modifier = Modifier.weight(1f)
                    )

                    FormCell(
                        label = "Rod length",
                        value = sheet.rodLgth,
                        modifier = Modifier.weight(1f)
                    )
                }

                FormRow(
                    label = "Rod condition",
                    value = sheet.rodCondition
                )

                FormRow(
                    label = "Bore diameter",
                    value = sheet.boreDia
                )

                FormRow(
                    label = "Bore condition",
                    value = sheet.boreCondition
                )

                FormSection(
                    title = "INSPECTION"
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CheckRow(
                            label = "Gland",
                            checked = sheet.gland
                        )

                        CheckRow(
                            label = "Eyes",
                            checked = sheet.eyes
                        )

                        CheckRow(
                            label = "Rod threads",
                            checked = sheet.rodThreads
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CheckRow(
                            label = "Hoses",
                            checked = sheet.hoses
                        )

                        CheckRow(
                            label = "Pin wipers",
                            checked = sheet.pinWipers
                        )

                        CheckRow(
                            label = "Fitting",
                            checked = sheet.fittings
                        )
                    }
                }
                CheckRow(
                    label = "Machined",
                    checked = sheet.isMachined
                )

                FormSection(
                    title = "PRICING"
                )

                PriceRow(
                    label = "seal kit",
                    price = sheet.washersPrice
                )

                PriceRow(
                    label = "Washers",
                    price = sheet.fittingsPrice
                )

                HorizontalDivider()

                PriceRow(
                    label = " Total labour",
                    price = sheet.totalLabour,
                    bold = true
                )
            }
        } ?: Text(
            text = "select a Worksheet",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private  fun FormSection(
    title: String
){
    HorizontalDivider(
        modifier = Modifier.padding(top = 4.dp)
    )

    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun FormRow(
    label: String,
    value: String
){
    FormCell(
        label = label,
        value = value,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private  fun FormCell (
    label: String,
    value: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            .padding(8.dp)
    ){
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value.ifBlank{
                "_"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CheckRow(
    label: String,
    checked: Boolean
){
    Text(
        text = if (checked){
            "$label : yes"
        } else {
            "$label: no"
        },
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 3.dp)
    )
}

@Composable
private fun PriceRow(
    label: String,
    price: Double,
    bold: Boolean = false
){
    val textWeight = if (bold){
        FontWeight.Bold
    } else {
        FontWeight.Normal
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
       Text(
           text = label,
           fontWeight = textWeight
       )

        Text(
            text = "$${"%.2f".format(price)}",
            fontWeight = textWeight
        )
    }
}