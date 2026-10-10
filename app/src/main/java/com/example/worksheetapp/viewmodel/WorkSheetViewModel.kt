package com.example.worksheetapp.viewmodel



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.worksheetapp.data.WorkSheetDatabase
import com.example.worksheetapp.data.WorkSheetRepository
import com.example.worksheetapp.data.Worksheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkSheetViewModel(
    application: Application
) : AndroidViewModel(application){

    // The ViewModel uses the Repository to communicate with the DAO.
    private val repository : WorkSheetRepository

    // UI can observe all worksheets
    val allWorksheet: LiveData<List<Worksheet>>

    // UI can observe only worksheets that are machined
    val machinedWorkSheetState: LiveData<List<Worksheet>>

    // Private version can be changed by the ViewModel.
    private val _addWorkSheetSuccess = MutableStateFlow(false)

    // public version can only be read by the UI
    val addWorkSheetSuccess : StateFlow<Boolean> =
        _addWorkSheetSuccess.asStateFlow()

    // Stores everything the user types/selects in the add Worksheet form
    data class AddWorkSheetState(
        val customer: String = "",
        val description: String = "",
        val tech: String = "",
        val supervisor: String = "",
        val workOrder: String = "",
        val tag: String = "",

        val rodDia: String = "",
        val rodLgth: String ="",
        val rodCondition: String = "",
        val boreDia: String ="",
        val boreCondition : String = "",


        val gland: Boolean = false,
        val eyes: Boolean = false,
        val rodThreads: Boolean = false,
        val hoses: Boolean = false,
        val pinWipers: Boolean = false,
        val fittings: Boolean = false,

        val sealKitPrice: String = "",
        val washersPrice: String = "",
        val fittingsPrice: String = "",
        val totalLabour: String = "",

        val isMachined: Boolean = false,

        // the location of all attached photos
        val photoPaths: List<String> = emptyList(),

        val isSaving : Boolean = false,

        val errors: List<String> = emptyList()
    )
// private version that the ViewModel can change
    private val _addWorkSheetState =
        MutableStateFlow(AddWorkSheetState())

    // public version that the UI can read
    val addWorkSheetState: StateFlow<AddWorkSheetState> =
        _addWorkSheetState.asStateFlow()

    init{
        // Get the database, then get its DAO
        val dao = WorkSheetDatabase
            .getDatabase(application)
            .workSheetDao()

        // Give the DAO to the Repository.
        repository = WorkSheetRepository(dao)

        // Convert the Repository's Flow values to liveData.
        allWorksheet = repository.worksheets.asLiveData()

        // only machined worksheet
        machinedWorkSheetState = repository.machinedWorksheets.asLiveData()
    }

    fun addPhoto(path: String){
        if (_addWorkSheetState.value.isSaving ||
            _addWorkSheetSuccess.value
            ){
            return
        }
        _addWorkSheetState.update {  current ->
            current.copy(
                photoPaths = (current.photoPaths + path).distinct()
            )
        }
    }

    fun validateAndAddWorkSheet() {
        val state = _addWorkSheetState.value

        //Ignore repeated save requests.
        if (state.isSaving || _addWorkSheetSuccess.value) {
            return
        }
        val errors = mutableListOf<String>()

        // Validate required text fields

        if (state.customer.isBlank()) {
            errors.add("Customer is required")
        }

        if (state.workOrder.isBlank()) {
            errors.add("work order is required")
        }

        // Convert price/labour text into number
        val sealKitPrice = state.sealKitPrice.toDoubleOrNull()
        val washersPrice = state.washersPrice.toDoubleOrNull()
        val fittingsPrice = state.fittingsPrice.toDoubleOrNull()
        val totalLabour = state.totalLabour.toDoubleOrNull()


        if (sealKitPrice == null || !sealKitPrice.isFinite() || sealKitPrice < 0) {
            errors.add("seal kit price must be a valid number")
        }

        if (washersPrice == null || !washersPrice.isFinite() || washersPrice < 0) {
            errors.add("Washers price must be a valid number ")
        }

        if (fittingsPrice == null || !fittingsPrice.isFinite() || fittingsPrice < 0) {
            errors.add("Fittings price must be a valid number")
        }

        if (totalLabour == null || !totalLabour.isFinite() || totalLabour < 0) {
            errors.add("Total labour must be a valid number")
        }

        // stop here if the form contains errors
        if (errors.isNotEmpty()) {
            _addWorkSheetState.update {
                it.copy(errors = errors)
            }
            _addWorkSheetSuccess.value = false
            return
        }
        // Build the real Worksheet object that room can save
        val worksheet = Worksheet(
            customer = state.customer,
            description = state.description,
            tech = state.tech,
            supervisor = state.supervisor,
            workOrder = state.workOrder,
            tag = state.tag,

            rodDia = state.rodDia,
            rodLgth = state.rodLgth,
            rodCondition = state.rodCondition,
            boreDia = state.boreDia,
            boreCondition = state.boreCondition,

            gland = state.gland,
            eyes = state.eyes,
            rodThreads = state.rodThreads,
            hoses = state.hoses,
            pinWipers = state.pinWipers,
            fittings = state.fittings,

            sealKitPrice = sealKitPrice!!,
            washersPrice = washersPrice!!,
            fittingsPrice = fittingsPrice!!,
            totalLabour = totalLabour!!,

            isMachined = state.isMachined
        )
        // The repository call below performs the save once
        _addWorkSheetState.update {
            it.copy(
                isSaving = true,
                errors = emptyList()
            )
        }

        viewModelScope.launch {
            try {
                // Save the worksheet and all its photo records
                repository.addWorksheet(
                    worksheet = worksheet,
                    photoPaths = state.photoPaths
                )

                _addWorkSheetSuccess.value = true
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled

            } catch (exception: Exception) {
                // keep the form and photos so the user can retry.

                _addWorkSheetState.update {
                    it.copy(
                        errors = listOf(
                            "Could not save the worksheet. Please try again"
                        )
                    )
                }
            } finally {
                // runs whether saving succeeds or fails
                _addWorkSheetState.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }

    fun updateFormState(
        customer: String? = null,
        description: String? = null,
        tech: String? = null,
        supervisor: String? = null,
        workOrder: String? = null,
        tag: String? = null,

        rodDia: String? = null,
        rodLgth: String? = null,
        rodCondition: String? = null,
        boreDia: String? = null,
        boreCondition: String? = null,

        gland: Boolean? = null,
        eyes: Boolean? = null,
        rodThreads: Boolean? = null,
        hoses: Boolean? = null,
        pinWipers: Boolean? = null,
        fittings: Boolean? = null,

        sealKitPrice: String? = null,
        washersPrice: String? = null,
        fittingsPrice: String? = null,
        totalLabour: String? = null,

        isMachined: Boolean? = null
    ){
       _addWorkSheetState.update { current ->
           current.copy(
               customer = customer ?: current.customer,
               description = description ?: current.description,
               tech = tech ?: current.tech,
               supervisor = supervisor ?: current.supervisor,
               workOrder = workOrder ?: current.workOrder,
               tag = tag ?: current.tag,

               rodDia = rodDia ?: current.rodDia,
               rodLgth = rodLgth ?: current.rodLgth,
               rodCondition = rodCondition ?: current.rodCondition,
               boreDia = boreDia ?: current.boreDia,
               boreCondition = boreCondition ?: current.boreCondition,

               gland = gland ?: current.gland,
               eyes = eyes ?: current.eyes,
               rodThreads = rodThreads ?: current.rodThreads,
               hoses = hoses ?: current.hoses,
               pinWipers = pinWipers ?: current.pinWipers,
               fittings = fittings ?: current.fittings,

               sealKitPrice = sealKitPrice ?: current.sealKitPrice,
               washersPrice = washersPrice ?: current.washersPrice,
               fittingsPrice = fittingsPrice ?: current.fittingsPrice,
               totalLabour = totalLabour ?: current.totalLabour,

               isMachined = isMachined ?: current.isMachined

           )

       }
    }

    fun toggleMachined(worksheet : Worksheet){
        val updateWorksheet = worksheet.copy(
            isMachined = !worksheet.isMachined
        )

        update(updateWorksheet)
    }
    fun resetSuccessState() {
        _addWorkSheetSuccess.value = false

        // clear the form and attachment list for the next worksheet
        _addWorkSheetState.value = AddWorkSheetState()
    }

    // update

    fun update(workSheet: Worksheet){
        viewModelScope.launch {
            repository.updateWorksheet(workSheet)
        }
    }

    // delete
    fun delete(workSheet: Worksheet) {
        viewModelScope.launch {
            repository.deleteWorksheet(workSheet)
        }
    }
}