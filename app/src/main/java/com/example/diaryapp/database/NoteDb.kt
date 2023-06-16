package com.example.diaryapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "notes")
data class NoteDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "noteTitle")
    val noteTitle: String,
    @ColumnInfo(name = "noteText")
    val noteText: String,
    @ColumnInfo(name = "noteMood")
    val noteMood: Int,
    @ColumnInfo(name = "noteDate")
    val noteDate: Date,
    @ColumnInfo(name = "notePhoto")
    val photoUrl: String? = null, // Добавляем поле для хранения ссылки на фото

    @ColumnInfo(name = "activityIds")
    @TypeConverters(Converters::class)
    var activityIds: List<ActivitiesDb>

)
