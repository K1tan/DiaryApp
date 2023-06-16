package com.example.diaryapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.diaryapp.screens.RepeatOption
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date

@Entity(tableName = "tasks")
data class TaskDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "taskTitle")
    val taskTitle: String,
    @ColumnInfo(name = "taskDesc")
    val taskDesc: String,
    @ColumnInfo(name = "taskDate")
    val date: String,
    @ColumnInfo(name = "taskTime")
    val time: Calendar,
    @ColumnInfo(name = "taskRepeatOption")
    val repeatOption: RepeatOption,
    @ColumnInfo(name = "checkboxes")
    @TypeConverters(DateConverter::class)
    val checkboxes: MutableList<Boolean> = mutableListOf(),
    @ColumnInfo(name = "checkboxesText")
    @TypeConverters(DateConverter::class)
    val checkboxesText: MutableList<String> = mutableListOf()
)


class DateConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDate(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromRepeatOption(value: RepeatOption): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRepeatOption(value: String): RepeatOption {
        val gson = Gson()
        val type = object : TypeToken<RepeatOption>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun toCalendar(value: Long): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = value
        }
    }

    @TypeConverter
    fun fromBooleanList(checkboxes: List<Boolean>): String {
        return gson.toJson(checkboxes)
    }

    @TypeConverter
    fun toBooleanList(checkboxesString: String): List<Boolean> {
        val type = object : TypeToken<List<Boolean>>() {}.type
        return gson.fromJson(checkboxesString, type)
    }

    @TypeConverter
    fun fromStringList(stringList: List<String>?): String? {
        return gson.toJson(stringList)
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }
}