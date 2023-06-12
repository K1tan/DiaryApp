package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.database.TaskDb
import com.example.diaryapp.screens.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow(emptyList<TaskDb>())
    val tasks: StateFlow<List<TaskDb>> = _tasks

    private val _filteredTasks = MutableStateFlow<List<TaskDb>>(emptyList())
    val filteredTasks: StateFlow<List<TaskDb>> = _filteredTasks

    private val _task = MutableStateFlow<TaskDb?>(null)
    val task: StateFlow<TaskDb?> = _task
    fun getAllTasks(db: MainDb) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = db.getDao().getAllTasks()
            _tasks.value = tasks
            _filteredTasks.value = tasks
        }
    }
    fun searchTasks(searchQuery: String){
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = if (searchQuery.isNotBlank()) {
                db.getDao().searchTasksByText(searchQuery)
            } else {
                db.getDao().getAllTasks()
            }
            _filteredTasks.value=tasks
        }
    }
    fun getTaskById(db: MainDb, taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = db.getDao().getTaskById(taskId)
            //_note.value = note
            _task.emit(task)
        }
    }
    suspend fun updateTask(db: MainDb, task: TaskDb) {
        withContext(Dispatchers.IO) {
            db.getDao().updateTask(task)
        }
    }
    fun deleteTaskById(db: MainDb, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().deleteByTaskId(id)
            val updatedNotes = _tasks.value.filter { it.id != id }
            _tasks.value = updatedNotes
        }
    }
}