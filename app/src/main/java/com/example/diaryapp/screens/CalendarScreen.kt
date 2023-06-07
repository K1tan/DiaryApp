package com.example.diaryapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.diaryapp.bottom_nav.BottomNavItem

@Composable
fun CalendarScreen() {
    Text(
        text = "Настройки", modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(), textAlign = TextAlign.Center
    )
}