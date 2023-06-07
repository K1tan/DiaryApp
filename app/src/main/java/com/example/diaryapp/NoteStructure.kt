package com.example.diaryapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.diaryapp.database.ActivitiesDb
import java.util.Date

class NoteStructure {
    var noteTitle: String by mutableStateOf("")
    var noteText: String by mutableStateOf("")
    var noteDate: Date by mutableStateOf(Date())
    var noteMood: Int by mutableStateOf(R.drawable.ic_normal)
    var selectedActivities by mutableStateOf(emptyList<ActivitiesDb>())
}


