package com.example.worksheetapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Dao = Date access object
// This is the part that directly talks to the Room database
@Dao
interface WorkSheetDao{
    // Get every worksheet from the database
    @Query("SELECT * FROM worksheet") // this gets the tables name
    fun getAll(): Flow<List<Worksheet>>

    // Add a new worksheet to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(worksheet: Worksheet): Long

    // Update an existing worksheet
    @Update
    suspend fun update(worksheet: Worksheet)

    // delete an existing worksheet.
    @Delete
    suspend fun delete(worksheet: Worksheet)

    // Get only worksheets where machining has been completed
    @Query("SELECT * FROM worksheet WHERE isMachined = 1")
    fun getMachined(): Flow<List<Worksheet>>

    // User if you want o insert sample worksheets
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSampleWorksheets(worksheets: List<Worksheet>)

    // Save a list of photo records
    @Insert
    suspend fun insertPhotos(
        photos: List<Worksheetphoto>
    )

    // Observe photos attached to one worksheet.
    @Query("""
        SELECT * FROM worksheet_photos
        WHERE worksheetId = :worksheetId
        ORDER BY id
    """
    )
    fun getPhotosForWorksheet(
        worksheetId: Int
    ):  Flow<List<Worksheetphoto>>

    // Save the worksheet and its photo as one operation.
    @Transaction
    suspend fun insertWorksheetWithPhotos(
        worksheet: Worksheet,
        photoPaths: List<String>
    ): Long {
        //First save the worksheet
        val worksheetId = insert(worksheet)

        // Worksheet uses an Int ID; check before converting.
        check(worksheetId in 1..Int.MAX_VALUE.toLong()){
            "Worksheet ID is out of range"
        }

        // Turn each file path into a photo database record.
        val photos = photoPaths.distinct().map { path ->
            Worksheetphoto(
                worksheetId = worksheetId.toInt(),
                photoPath  = path
            )
        }

        // only insert photos when there are some
        if (photos.isNotEmpty()){
            insertPhotos(photos)
        }
        return worksheetId
    }

}