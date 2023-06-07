package com.example.diaryapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.diaryapp.other.Constants

@Database(entities = [NoteDb::class, TaskDb::class, ActivitiesDb::class], version = 3)
@TypeConverters(DateConverter::class, Converters::class)
abstract class MainDb:RoomDatabase() {
    abstract fun getDao(): Dao
    companion object{
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "test.db"
            )
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        insertDefaultActivities(db)
                    }
                })
                .build()
                //.fallbackToDestructiveMigration() - удалит имеющиеся данные
        }
        private fun insertDefaultActivities(db: SupportSQLiteDatabase) {
            val defaultActivities = Constants.DEFAULT_ACTIVITIES

            // Insert default activities into the ActivitiesDb table
            defaultActivities.forEach { activity ->
                val contentValues = ContentValues()
                contentValues.put("name", activity.name)
                db.insert("activities", SQLiteDatabase.CONFLICT_REPLACE, contentValues)
            }
        }
    }
}