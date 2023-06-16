package com.example.diaryapp.screens

import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryapp.NoteStructure
import com.example.diaryapp.R
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDataScreen(navController: NavHostController, noteStructure: NoteStructure) {

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    Column {

        Row(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth()
                .padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.padding(start = 20.dp)) {
                TextField(
                    value = noteStructure.noteTitle, onValueChange = {
                        noteStructure.noteTitle = it
                    },
                    label = {
                        Text(
                            text = "Введите заголовок",
                        )
                    }, modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .background(backgroundColor),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(textColor = textColor)
                )

            }
            Image(
                painterResource(id = R.drawable.ic_submit), modifier = Modifier
                    .size(64.dp)
                    .fillMaxWidth(0.3f)
                    .clickable {
                        navController.navigate("screen_addNote") {
                            launchSingleTop = true
                            restoreState = true

                        }

                    }, contentDescription = "submitImage"
            )

        }
        TextField(
            value = noteStructure.noteText, onValueChange = {
                noteStructure.noteText = it
            },
            label = {
                Text(
                    text = "Введите текст",
                )
            }, modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight()
                .background(cardBackground)
                .padding(15.dp),

            colors = TextFieldDefaults.textFieldColors(textColor = textColor)
        )

    }

}
