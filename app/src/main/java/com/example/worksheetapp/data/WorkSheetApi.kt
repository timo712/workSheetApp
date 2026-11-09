package com.example.worksheetapp.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WorksheetApi {
    @GET("api/worksheets")
    suspend fun  getWorksheets(): List<WorksheetDto>

    @POST("api/worksheets")
    suspend fun  createWorksheet(
        @Body worksheet: WorksheetDto
    ): WorksheetDto

}