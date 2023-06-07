package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.ActivitiesDb
import com.example.diaryapp.screens.db
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.date.convertToDayStartEnd
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class NoteViewModel : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<NoteDb>())
    val notes: StateFlow<List<NoteDb>> = _notes

    private val _filteredNotes = MutableStateFlow<List<NoteDb>>(emptyList())
    val filteredNotes: StateFlow<List<NoteDb>> = _filteredNotes

    private val _note = MutableStateFlow<NoteDb?>(null)
    val note: StateFlow<NoteDb?> = _note

    private val _activities = MutableStateFlow<List<ActivitiesDb>>(emptyList())
    val activities: StateFlow<List<ActivitiesDb>> = _activities

    fun getAllNotes(db: MainDb) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = db.getDao().getAllNotes()
            _notes.value = notes
            _filteredNotes.value = notes
        }
    }
    fun searchNotes(searchQuery: String, selectedDate: Date?) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = if (selectedDate != null) {
                if (searchQuery.isNotBlank()) {
                    val (dayStart, dayEnd) = convertToDayStartEnd(selectedDate)
                    //Log.d("MyTag","дата и текст введены: $dayStart $dayEnd")
                    db.getDao().searchNotesByDateAndText(dayStart, dayEnd, searchQuery)

                } else {
                    val (dayStart, dayEnd) = convertToDayStartEnd(selectedDate)
                    //Log.d("MyTag","дата введена $dayStart $dayEnd")
                    db.getDao().getNotesByDate(dayStart, dayEnd)

                }
            } else {
                if (searchQuery.isNotBlank()) {
                    //Log.d("MyTag","текст введен")
                    db.getDao().searchNotesByText(searchQuery)
                } else {
                    //Log.d("MyTag","ничего не введено")
                    db.getDao().getAllNotes()
                }
            }

            _filteredNotes.value = notes
            //Log.d("MyTag","$notes")

        }
    }


    fun deleteNoteById(db: MainDb, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().deleteById(id)
            val updatedNotes = _notes.value.filter { it.id != id }
            _notes.value = updatedNotes
        }
    }
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
    fun loadActivities() {
        viewModelScope.launch {

            val activitiesList = db.getDao().getAllActivities()
            _activities.value = activitiesList

        }
    }
    fun deleteActivity(activity: ActivitiesDb) {
        viewModelScope.launch (Dispatchers.IO){
            db.getDao().deleteActivity(activity)
            val updatedActivities = _activities.value.toMutableList()
            updatedActivities.remove(activity)
            _activities.value = updatedActivities
        }
    }
}