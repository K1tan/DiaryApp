package com.example.diaryapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteDb: NoteDb)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteWithActivities(note: NoteDb): Long
    @Insert
    fun insertTask(taskDb: TaskDb)
    @Insert
    fun insertActivity(activity: ActivitiesDb)
    @Query("SELECT * FROM activities")
    suspend fun getAllActivities(): List<ActivitiesDb>
    @Delete
    suspend fun deleteActivity(activity: ActivitiesDb)
    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NoteDb>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskDb>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteByTaskId(id: Int)
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteDb?
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TaskDb?
    @Update()
    suspend fun update(note: NoteDb)
    @Update()
    suspend fun updateTask(task: TaskDb)

    @Query("SELECT * FROM tasks WHERE taskTitle LIKE '%' || :searchQuery || '%' OR taskDesc LIKE '%' || :searchQuery || '%'")
    fun searchTasksByText(searchQuery: String): List<TaskDb>
    @Query("SELECT * FROM notes WHERE noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%'")
    fun searchNotesByText(searchQuery: String): List<NoteDb>

    //@Query("SELECT * FROM notes WHERE noteDate = :selectedDate")
    @Query("SELECT * FROM notes WHERE noteDate BETWEEN :dayStart AND :dayEnd")
    fun getNotesByDate(dayStart: Date, dayEnd: Date): List<NoteDb>

    //@Query("SELECT * FROM notes WHERE noteDate = :selectedDate AND (noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%')")
    @Query("SELECT * FROM notes WHERE noteDate BETWEEN :dayStart AND :dayEnd AND (noteTitle LIKE '%' || :searchQuery || '%' OR noteText LIKE '%' || :searchQuery || '%')")
    fun searchNotesByDateAndText(dayStart: Date, dayEnd: Date, searchQuery: String): List<NoteDb>
    @Query("UPDATE notes SET activityIds = :activityIds WHERE :activityId IN (SELECT id FROM activities)")
    suspend fun updateActivityIds(activityId: Int, activityIds: List<ActivitiesDb>)
    @Query("SELECT * FROM notes WHERE :activityId IN (SELECT id FROM activities)")
    suspend fun getNotesByActivityId(activityId: Int): List<NoteDb>


    @Query("SELECT COUNT(*) FROM activities WHERE id = :activityId")
    suspend fun getActivityCount(activityId: Int?): Int
}