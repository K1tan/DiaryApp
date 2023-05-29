package com.example.diaryapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import androidx.room.TypeConverter

@Entity(tableName = "tasks")
data class TaskDb (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "taskTitle")
    val taskTitle: String,
    @ColumnInfo(name = "taskDesc")
    val taskDesc: String,
    @ColumnInfo(name = "taskDate")
    val date: Date
        )

class DateConverter {
    @TypeConverter
    fun fromDate(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }
}