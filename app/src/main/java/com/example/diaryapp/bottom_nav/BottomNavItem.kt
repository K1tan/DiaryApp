package com.example.diaryapp.bottom_nav

import com.example.diaryapp.R

sealed class BottomNavItem(val title: String, val iconId: Int, val route: String) {
    object DiaryScreen : BottomNavItem("Дневник", R.drawable.baseline_menu_book_24, "screen_diary")
    object StatScreen : BottomNavItem("Статистика", R.drawable.baseline_bar_chart_24, "screen_stat")
    object AddNoteScreen : BottomNavItem("Запись", R.drawable.baseline_add_circle_outline_24, "screen_addNote")
    object AddTaskScreen : BottomNavItem("Задачи", R.drawable.baseline_task_24, "screen_addTask")
    object CalendarScreen : BottomNavItem("Календарь", R.drawable.baseline_calendar_month_24, "screen_calendar")
    object SettingsScreen : BottomNavItem("Настройки", R.drawable.baseline_settings_24, "screen_settings")
}
