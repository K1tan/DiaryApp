package com.example.diaryapp.other

import com.example.diaryapp.database.ActivitiesDb

object Constants {
    val DEFAULT_ACTIVITIES = listOf(
        ActivitiesDb(name = "Спорт"),
        ActivitiesDb(name = "Учеба"),
        ActivitiesDb(name = "Отдых"),
        ActivitiesDb(name = "Прогулка"),
        ActivitiesDb(name = "Работа")
    )
}