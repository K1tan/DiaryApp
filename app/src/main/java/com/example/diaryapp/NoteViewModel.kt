package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<NoteDb>())
    val notes: StateFlow<List<NoteDb>> = _notes


    private val _note = MutableStateFlow<NoteDb?>(null)
    val note: StateFlow<NoteDb?> = _note

    fun getAllNotes(db: MainDb) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = db.getDao().getAllNotes()
            _notes.value = notes
        }
    }
    fun deleteNoteById(db: MainDb, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().deleteById(id)
            val updatedNotes = _notes.value.filter { it.id != id }
            _notes.value = updatedNotes
        }
    }
    /*
    fun getNoteById(db: MainDb, noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = db.getDao().getNoteById(noteId)
            _note.value = note
        }
    }
    **/
    fun getNoteById(db: MainDb, noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = db.getDao().getNoteById(noteId)
            //_note.value = note
            _note.emit(note)
        }
    }
    suspend fun updateNote(db: MainDb, note: NoteDb) {
        withContext(Dispatchers.IO) {
            db.getDao().update(note)
        }
    }

}