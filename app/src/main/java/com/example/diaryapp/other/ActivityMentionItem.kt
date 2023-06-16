package com.example.diaryapp.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.SuperMoodColor
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs

@Composable
fun ActivityMentionItem(activityName: String, mentionCount: Int) {

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    SuperMoodColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mentionCount.toString(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = activityName,
            fontSize = 15.sp,
            color = textColor,
            modifier = Modifier.padding(start = 3.dp)
        )
    }
}

@Preview
@Composable
fun Preview() {
    ActivityMentionItem(activityName = "", mentionCount = 1)
}