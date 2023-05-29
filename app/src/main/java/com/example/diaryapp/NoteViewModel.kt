package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<NoteDb>())
    val notes: StateFlow<List<NoteDb>> = _notes

    fun getAllNotes(db: MainDb) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = db.getDao().getAllNotes()
            _notes.value = notes
        }
    }
}