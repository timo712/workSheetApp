package com.example.worksheetapp.data


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "worksheet_photos",
    foreignKeys = [
        ForeignKey(
            entity = Worksheet::class,
            parentColumns = ["id"],
            childColumns = ["worksheetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["worksheetId"])]
)
data class Worksheetphoto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Identifies the worksheet this photo belong to .
    val worksheetId: Int,

    // location of the photo in the phone's storage
    val photoPath: String
)