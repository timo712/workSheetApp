package com.example.worksheetapp.data

import kotlinx.coroutines.flow.Flow

// Repository class managing data operations between ViewModel  and the Dao
class WorkSheetRepository(private val dao: WorkSheetDao){
    val worksheets: Flow<List<Worksheet>> = dao.getAll() // stream of all work list objest
    val machinedWorksheets: Flow<List<Worksheet>> = dao.getMachined()

    // Adds a new worksheet with all its photo path
    suspend fun addWorksheet(worksheet: Worksheet, photoPaths: List<String> = emptyList()
    ): Long  { return dao.insertWorksheetWithPhotos(
        worksheet = worksheet,
        photoPaths = photoPaths
    )
    }

    // Observe photos belonging to a worksheet.
    fun getPhotosForWorksheet(
        worksheetId: Int
    ): Flow<List<Worksheetphoto>>{
        return  dao.getPhotosForWorksheet(worksheetId)
    }

    // Update an existing worksheet
    suspend fun updateWorksheet(worksheet: Worksheet) { dao.update(worksheet) }

    // Deletes a worksheet
    suspend fun  deleteWorksheet(worksheet: Worksheet) { dao.delete(worksheet) }

}