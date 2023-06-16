package com.example.diaryapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.database.ActivitiesDb
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivity(navController: NavHostController) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var activityName by remember { mutableStateOf("") }
    val noteViewModel: NoteViewModel = viewModel()
    val activities by noteViewModel.activities.collectAsState()
    val deleteBtnClicked = remember {
        mutableStateOf(false)
    }
    val dbNotes by noteViewModel.notes.collectAsState()
    lateinit var activityToDelete: ActivitiesDb

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    LaunchedEffect(Unit) {
        noteViewModel.getAllNotes(db)
    }
    LaunchedEffect(deleteBtnClicked.value) {
        noteViewModel.removeNonExistentActivitiesFromNotes(dbNotes)
        deleteBtnClicked.value = false
    }
    LaunchedEffect(Unit) {
        noteViewModel.loadActivities()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box() {
                TextField(
                    value = activityName, onValueChange = {
                        activityName = it
                    },
                    label = {
                        Text(
                            text = "Добавить занятие",
                        )
                    }, modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(cardBackground),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(textColor = textColor)
                )
            }
            Image(
                painterResource(id = R.drawable.ic_submit), modifier = Modifier
                    .size(56.dp)
                    .fillMaxWidth(0.2f)
                    .clickable {

                        if (activityName.filter { !it.isWhitespace() } != "") {
                            val activityDb = ActivitiesDb(
                                null,
                                activityName
                            )
                            Thread {
                                db
                                    .getDao()
                                    .insertActivity(activityDb)
                            }.start()
                            navController.navigate("screen_addNote") {
                                launchSingleTop = true
                                restoreState = true
                            }

                        }

                        navController.navigate("screen_addNote") {
                            launchSingleTop = true
                            restoreState = true

                        }
                    }, contentDescription = "submitImage"
            )
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(bottom = 64.dp)) {
            LazyColumn {
                items(activities.size) { index ->
                    Box {
                        if (showConfirmationDialog) {
                            AlertDialog(
                                modifier = Modifier.background(Color.Transparent),
                                onDismissRequest = { showConfirmationDialog = false },
                                title = {
                                    Text(
                                        text = "Удалить занятие?",
                                        color = Color.White
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Вы уверены, что хотите безвозвратно удалить занятие? Оно также удалится со всех ваших записей.",
                                        color = Color.White
                                    )
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            noteViewModel.deleteActivity(activityToDelete)
                                            showConfirmationDialog = false
                                            deleteBtnClicked.value = true
                                        },
                                        colors = ButtonDefaults.buttonColors(GreenSoft)
                                    ) {
                                        Text(text = "Удалить", color = Color.Red)
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = { showConfirmationDialog = false },
                                        colors = ButtonDefaults.buttonColors(GreenSoft)
                                    ) {
                                        Text(text = "Отмена", color = Color.Black)
                                    }
                                },
                                backgroundColor = cardBackground,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = activities[index].name,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                IconButton(onClick = {
                                    activityToDelete = activities[index]
                                    showConfirmationDialog = true
                                }) {
                                    Image(
                                        painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Удалить активность"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun Main() {
    AddActivity(NavHostController(LocalContext.current))
}