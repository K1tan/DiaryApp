package com.example.diaryapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

}