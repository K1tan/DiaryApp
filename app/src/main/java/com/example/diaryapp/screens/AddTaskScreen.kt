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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.R
import com.example.diaryapp.TaskViewModel
import com.example.diaryapp.date.DateInRussianFormat
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController) {
    val taskViewModel: TaskViewModel = viewModel()
    val dbTasks by taskViewModel.tasks.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredTasks by taskViewModel.filteredTasks.collectAsState()

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                colors = TextFieldDefaults.textFieldColors(textColor=textColor,containerColor = cardBackground),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    taskViewModel.searchTasks(searchQuery)
                },
                label = {
                    Row {
                        Image(
                            painterResource(id = R.drawable.search_icon),
                            contentDescription = "ic_search",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(text = "ПОИСК", fontSize = 15.sp)
                    }
                },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(0.5f)
                    .height(45.dp),
                onClick = {
                    navController.navigate("screen_addDataTask") {
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                colors = ButtonDefaults.buttonColors(GreenSoft),
            ) {
                Text(text = "Добавить", color = Color.Black, fontSize = 20.sp)
            }
        }
        Column(modifier = Modifier.padding(top = 20.dp)) {
            Box {
                LaunchedEffect(Unit) {
                    taskViewModel.getAllTasks(db)
                }
                LazyColumn {
                    if (dbTasks.isEmpty()) {
                        item {
                            Text(
                                text = "Здесь пусто, создайте задачу",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                color = textColor,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        items(filteredTasks) { task ->
                            Box(modifier = Modifier.clickable(onClick = {
                                val taskId = task.id ?: -1
                                navController.navigate(
                                    route = "screen_editTask/$taskId",
                                    builder = {
                                        launchSingleTop = true
                                        restoreState = true
                                    })
                            })) {
                                Card(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = CardDefaults.cardColors(cardBackground)
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = task.taskTitle,
                                                fontSize = 25.sp,
                                                color = textColor,
                                                lineHeight = 35.sp
                                            )
                                        }
                                        if (task.taskDesc.isNotBlank()) {
                                            Text(
                                                text = task.taskDesc,
                                                fontSize = 20.sp,
                                                color = textColor,
                                                modifier = Modifier.padding(bottom = 20.dp)
                                            )
                                        }
                                        Divider()

                                        val timeFormat =
                                            SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val formattedTime = timeFormat.format(task.time.time)
                                        //Log.d("MyTag","${formattedTime} and ${task.repeatOption}")
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = "дата: ${task.date}",
                                                fontSize = 15.sp,
                                                color = textColor,
                                                modifier = Modifier.padding(
                                                    top = 5.dp,
                                                    bottom = 10.dp
                                                )
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
    }
}
