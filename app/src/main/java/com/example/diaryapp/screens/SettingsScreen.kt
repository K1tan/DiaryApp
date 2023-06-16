package com.example.diaryapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs

@Composable
fun SettingsScreen(
    darkThemeEnabled: MutableState<Boolean>,
    remindersEnabled: MutableState<Boolean>,
    taskNotificationsEnabled: MutableState<Boolean>,
    navController: NavHostController,
) {

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor =
        if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor
        )

        // Выбор темы
        Text(
            text = "Тема",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp),
            color = textColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Switch(
                checked = darkThemeEnabled.value,
                onCheckedChange = { isChecked ->
                    darkThemeEnabled.value = isChecked
                    Prefs.putBoolean("darkTheme", isChecked)
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Тёмная тема", color = textColor)
        }

        // Напоминания
        Text(
            text = "Напоминания",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp),
            color = textColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Switch(
                checked = remindersEnabled.value,
                onCheckedChange = { isChecked ->
                    remindersEnabled.value = isChecked
                    Prefs.putBoolean("reminders", isChecked)
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Включить напоминания", color = textColor
            )
        }

        // Уведомления от задач
        Text(
            text = "Уведомления от задач",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp),
            color = textColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Switch(
                checked = taskNotificationsEnabled.value,
                onCheckedChange = { isChecked ->
                    taskNotificationsEnabled.value = isChecked
                    Prefs.putBoolean("taskNotifications", isChecked)
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Включить уведомления от задач", color = textColor
            )
        }

        // Добавить занятие
        Button(
            onClick = {
                navController.navigate("screen_addActivity") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(GreenSoft)
        ) {
            Text(text = "Добавить занятие", color = Color.Black)
        }
    }
}