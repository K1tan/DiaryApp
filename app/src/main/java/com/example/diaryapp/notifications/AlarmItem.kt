package com.example.diaryapp.notifications

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)