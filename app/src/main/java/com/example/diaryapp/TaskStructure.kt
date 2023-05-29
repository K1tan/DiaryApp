package com.example.diaryapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Date


class TaskStructure {
    var taskTitle: String by mutableStateOf("")
    var taskDesc: String by mutableStateOf("")
    var taskDate: Date by mutableStateOf(Date())
}


