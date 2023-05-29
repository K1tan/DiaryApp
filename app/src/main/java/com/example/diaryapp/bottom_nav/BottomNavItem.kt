package com.example.diaryapp.bottom_nav

import com.example.diaryapp.R

sealed class BottomNavItem(val title:String, val iconId:Int, val route:String){
    object DiaryScreen: BottomNavItem("Дневник", R.drawable.diary_icon, "screen_diary")
    object StatScreen: BottomNavItem("Статистика", R.drawable.stat_icon, "screen_stat")
    object AddNoteScreen: BottomNavItem("Запись", R.drawable.add_note_icon, "screen_addNote")
    object AddTaskScreen: BottomNavItem("Задачи", R.drawable.task_icon, "screen_addTask")
    object CalendarScreen: BottomNavItem("Календарь", R.drawable.calendar_icon, "screen_calendar")
    object SettingsScreen: BottomNavItem("Настройки", R.drawable.settings_icon, "screen_settings")
}
