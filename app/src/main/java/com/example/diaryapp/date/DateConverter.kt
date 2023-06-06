package com.example.diaryapp.date

import java.util.*



fun convertToDayStartEnd(selectedDate: Date): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    // Set the time to the beginning of the day (00:00)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val dayStart = calendar.time

    // Set the time to the end of the day (23:59)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val dayEnd = calendar.time

    return Pair(dayStart, dayEnd)
}