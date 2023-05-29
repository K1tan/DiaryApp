package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.database.TaskDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(emptyList<TaskDb>())
    val tasks: StateFlow<List<TaskDb>> = _tasks

    fun getAllTasks(db: MainDb) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = db.getDao().getAllTasks()
            _tasks.value = tasks
        }
    }
}