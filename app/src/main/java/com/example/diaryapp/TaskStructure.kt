package com.example.diaryapp

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import com.example.diaryapp.screens.RepeatOption
import java.util.Calendar
import java.util.Date


@SuppressLint("MutableCollectionMutableState")
class TaskStructure {
    var taskTitle: String by mutableStateOf("")
    var taskDesc: String by mutableStateOf("")
    var taskDate: String by mutableStateOf("")
    var taskTime: Calendar by mutableStateOf(Calendar.getInstance())
    var taskRepeatOption: RepeatOption by mutableStateOf(RepeatOption.NEVER)
    var checkboxes: MutableList<Boolean> = mutableStateListOf()
    var checkboxesText: MutableList<String> = mutableStateListOf()
}




