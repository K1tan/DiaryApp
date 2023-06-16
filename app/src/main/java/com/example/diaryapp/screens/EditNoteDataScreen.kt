package com.example.diaryapp.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.ContextProvider
import com.example.diaryapp.NoteStructure
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteDataScreen(
    navController: NavHostController,
    noteStructure: NoteStructure,
    noteId: Int?
) {
    val noteViewModel: NoteViewModel = viewModel()
    val dbNote by noteViewModel.note.collectAsState()
    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    LaunchedEffect(Unit) {
        if (noteId != null) {
            noteViewModel.getNoteById(db, noteId)
        }
    }
    LaunchedEffect(dbNote) {
        dbNote?.let { note ->

            noteStructure.noteTitle = note.noteTitle
            noteStructure.noteText = note.noteText
        }
    }
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
                        .background(cardBackground),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                )

            }
            Image(
                painterResource(id = R.drawable.ic_submit), modifier = Modifier
                    .size(64.dp)
                    .fillMaxWidth(0.3f)
                    .clickable {
                        if (noteStructure.noteText.filter { !it.isWhitespace() } != "") {
                            dbNote?.let { note ->
                                val updatedNote = note.copy(
                                    noteText = noteStructure.noteText,
                                    noteTitle = noteStructure.noteTitle
                                )
                                noteViewModel.viewModelScope.launch {
                                    noteViewModel.updateNote(db, updatedNote)
                                }

                            }
                            navController.navigate("screen_diary") {
                                launchSingleTop = true
                                restoreState = true

                            }
                            noteStructure.noteTitle = ""
                            noteStructure.noteText = ""
                        } else Toast
                            .makeText(
                                ContextProvider.getContext(),
                                "Пожалуйста введите текст заметки.",
                                Toast.LENGTH_SHORT
                            )
                            .show()

                    }, contentDescription = "submitImage"
            )

        }
        TextField(
            value = noteStructure.noteText,
            onValueChange = {
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

            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )

    }
}