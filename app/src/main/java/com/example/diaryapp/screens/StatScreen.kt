package com.example.diaryapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StatScreen() {
    var counter = rememberSaveable {
        mutableStateOf(0)
    }
    Text(
        text = "Статистика ${counter.value}", modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .clickable { counter.value++ }, textAlign = TextAlign.Center
    )
}
