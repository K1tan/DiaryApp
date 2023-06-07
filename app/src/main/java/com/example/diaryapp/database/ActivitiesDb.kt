package com.example.diaryapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivitiesDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val name: String
)