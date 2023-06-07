package com.example.diaryapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(value: List<ActivitiesDb>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String?): List<ActivitiesDb>? {
        val listType = object : TypeToken<List<ActivitiesDb>>() {}.type
        return Gson().fromJson(value, listType)
    }
}