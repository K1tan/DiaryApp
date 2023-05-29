package com.example.diaryapp.bottom_nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diaryapp.NoteStructure
import com.example.diaryapp.TaskStructure

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
            DiaryScreen()
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
        composable("screen_addDataTask"){
            AddTaskDataScreen(navController = navHostController, taskStructure)
        }

    }
}