package com.example.diaryapp.bottom_nav

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diaryapp.NoteStructure
import com.example.diaryapp.TaskStructure
import com.example.diaryapp.screens.AddActivity
import com.example.diaryapp.screens.AddNoteDataScreen
import com.example.diaryapp.screens.AddNoteScreen
import com.example.diaryapp.screens.AddTaskDataScreen
import com.example.diaryapp.screens.AddTaskScreen
import com.example.diaryapp.screens.CalendarScreen
import com.example.diaryapp.screens.DiaryScreen
import com.example.diaryapp.screens.EditNoteDataScreen
import com.example.diaryapp.screens.EditTaskScreen
import com.example.diaryapp.screens.SettingsScreen
import com.example.diaryapp.screens.StatScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {
    val noteStructure = remember {
        NoteStructure()
    }
    val taskStructure = remember {
        TaskStructure()
    }


    NavHost(navController = navHostController, startDestination = "screen_diary"){

        composable("screen_diary"){
            DiaryScreen(navController = navHostController)
        }
        composable("screen_stat"){
            StatScreen()
        }
        composable("screen_addNote"){
            AddNoteScreen(navController = navHostController, noteStructure)
        }
        composable("screen_addTask"){
            AddTaskScreen(navController = navHostController)
        }
        composable("screen_calendar"){
            CalendarScreen()
        }
        composable("screen_settings"){
            SettingsScreen()
        }
        composable("screen_addDataNote"){
            AddNoteDataScreen(navController = navHostController, noteStructure)
        }
        composable("screen_editTask/{taskId}"){backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            EditTaskScreen(navController = navHostController, taskStructure = taskStructure, taskId)
        }
        composable("screen_editDataNote/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            EditNoteDataScreen(navController = navHostController, noteStructure, noteId)
        }
        composable("screen_addDataTask"){
            AddTaskDataScreen(navController = navHostController, taskStructure)
        }
        composable("screen_addActivity"){
            AddActivity(navController = navHostController)
        }

    }
}

