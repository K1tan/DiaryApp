package com.example.diaryapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface Dao {
    @Insert
    fun insertNote(noteDb: NoteDb)

    @Insert
    fun insertTask(taskDb: TaskDb)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NoteDb>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskDb>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteDb?

    @Update()
    suspend fun update(note: NoteDb)

    /*
    @Query("SELECT * FROM notes WHERE noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): List<NoteDb>
    */

    //@Query("SELECT * FROM notes WHERE noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%'")

    @Query("SELECT * FROM notes WHERE noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%'")
    fun searchNotesByText(searchQuery: String): List<NoteDb>

    //@Query("SELECT * FROM notes WHERE noteDate = :selectedDate")
    @Query("SELECT * FROM notes WHERE noteDate BETWEEN :dayStart AND :dayEnd")
    fun getNotesByDate(dayStart: Date, dayEnd: Date): List<NoteDb>

    //@Query("SELECT * FROM notes WHERE noteDate = :selectedDate AND (noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%')")
    @Query("SELECT * FROM notes WHERE noteDate BETWEEN :dayStart AND :dayEnd AND (noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%')")
    fun searchNotesByDateAndText(dayStart: Date, dayEnd: Date, searchQuery: String): List<NoteDb>

}