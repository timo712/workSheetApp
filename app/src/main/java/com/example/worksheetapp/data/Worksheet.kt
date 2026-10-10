package com.example.worksheetapp.data

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "worksheet")
data class Worksheet(
    // Room automatically creates a unique ID for each worksheet.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // General job information.
    val customer: String,
    val description: String,
    val tech: String,
    val supervisor: String,
    val workOrder: String,
    val tag: String,

    // Rod and bore information
    val rodDia: String,
    val rodLgth: String,
    val rodCondition: String,
    val boreDia: String,
    val boreCondition: String,

    // Yes/No worksheet checks
    val gland: Boolean,
    val eyes: Boolean,
    val rodThreads: Boolean,
    val hoses: Boolean,
    val pinWipers: Boolean,
    val fittings: Boolean,

    // pricing / labour values
    val sealKitPrice: Double,
    val washersPrice: Double,
    val fittingsPrice: Double,
    val totalLabour: Double,

    // Tracks whether machining has been completed.
    val isMachined: Boolean = false,

    // Store the photo's location. null means no photo is attached
    val photoPath: String? = null
)
