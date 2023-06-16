package com.example.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.database.ActivitiesDb
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.date.convertToDayStartEnd
import com.example.diaryapp.screens.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
    private val _activityCounts = MutableStateFlow<Map<Int?, Int>>(emptyMap())
    val activityCounts: StateFlow<Map<Int?, Int>> = _activityCounts

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
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().deleteActivity(activity)
            val updatedActivities = _activities.value.toMutableList()
            updatedActivities.remove(activity)
            _activities.value = updatedActivities
        }
    }

    suspend fun doesActivityExist(activityId: Int?): Boolean {
        return if (activityId != null) {
            db.getDao().getActivityCount(activityId) > 0
        } else {
            false
        }
    }

    suspend fun removeNonExistentActivitiesFromNotes(notes: List<NoteDb>) {
        withContext(Dispatchers.IO) {
            val idToRemove = mutableListOf<Int>()
            val updatedNotes = mutableListOf<NoteDb>()
            notes.forEach { note ->
                val existingActivityIds = note.activityIds.filter { activity ->
                    val doesExist = doesActivityExist(activityId = activity.id)
                    if (!doesExist && !idToRemove.contains(activity.id)) {
                        activity.id?.let { idToRemove.add(it) }
                    }
                    doesExist
                }
                note.activityIds = existingActivityIds
                updatedNotes.add(note)

            }

            updatedNotes.forEach { note ->
                val updatedActivityIds = note.activityIds.filterNot { activity ->
                    idToRemove.contains(activity.id)
                }
                note.activityIds = updatedActivityIds
                updateNoteInDatabase(note)
            }
        }
    }

    private suspend fun updateNoteInDatabase(note: NoteDb) {
        withContext(Dispatchers.IO) {
            db.getDao().update(note)
        }
    }

    fun countActivityMentions(): Flow<Map<ActivitiesDb, Int>> = flow {
        val activityCountMap = mutableMapOf<ActivitiesDb, Int>()

        _notes.value.forEach { note ->
            note.activityIds.forEach { activity ->
                val count = activityCountMap.getOrDefault(activity, 0)
                activityCountMap[activity] = count + 1
            }
        }

        emit(activityCountMap)
    }

    suspend fun getTopActivitiesByMood(mood: Int): List<String> = viewModelScope.async {
        db.getDao().getTopActivitiesByMood(mood)
    }.await()

}