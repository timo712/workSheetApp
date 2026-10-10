package com.example.worksheetapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Worksheet::class, Worksheetphoto::class], version = 2) // this tells room( create a Room database )
abstract class WorkSheetDatabase : RoomDatabase() {
    abstract fun workSheetDao(): WorkSheetDao // Provides access to WorkSheetDao

    companion object{
        @Volatile
        private var INSTANCE: WorkSheetDatabase? = null

        //Upgrade an existing version-1 database to version 2.
        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {

                // check whether the earlier single-photo colum exists
                var hasPhotoPath = false

                db.query("PRAGMA table_info(`worksheet`)").use { cursor ->
                    val nameIndex = cursor.getColumnIndexOrThrow("name")

                    while(cursor.moveToNext()){
                        if(cursor.getString(nameIndex) == "photoPath"){
                            hasPhotoPath = true
                        }
                    }
                }

                // Keep the worksheet table consistent with Worksheet.kt.
                if (!hasPhotoPath){
                    db.execSQL(
                        "ALTER TABLE `worksheet`" +
                          " ADD COLUMN `photoPath` TEXT"
                    )
                }

                // Create the table that support multiple photos.
                db.execSQL(
                    """
                     CREATE TABLE IF NOT EXISTS `worksheet_photos` (
                     `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                     `worksheetId` INTEGER NOT NULL,
                     `photoPath` TEXT NOT NULL,
                     FOREIGN KEY(`worksheetId`)
                            REFERENCES `worksheet`(`id`)
                            ON UPDATE NO ACTION 
                            ON DELETE CASCADE 
                     )
                    """.trimIndent()
                )

                // Speed up finding photos by worksheet ID.

                db.execSQL(
                    """
                        CREATE INDEX IF NOT EXISTS
                           `index_worksheet_photos_worksheetId`
                        ON `worksheet_photos` (`worksheetId`)
                    """.trimIndent()
                )

                // Move earlier single-photo attachments into the new table.

                db.execSQL(
                    """
                        INSERT INTO worksheet_photos(
                            worksheetId,
                            photoPath
                        )
                        SELECT id, photoPath 
                        FROM worksheet
                        WHERE photoPath IS NOT NULL
                            AND photoPath != ''
                    """.trimIndent()
                )
            }
        }

        //Singleton pattern to get or create the database instance
        fun getDatabase(context: Context): WorkSheetDatabase{
            //return INSTANCE?: synchronized(this) this creates the database if not already made.
            return INSTANCE?: synchronized(this) {
                //  Room.databaseBuilder this actually builds the database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkSheetDatabase::class.java,
                    "worksheet_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}