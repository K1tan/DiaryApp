package com.example.diaryapp.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryapp.R
import com.example.diaryapp.TaskStructure
import com.example.diaryapp.database.TaskDb
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColor
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDataScreen(navController: NavHostController, taskStructure: TaskStructure) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {
        Row(
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth()
                .padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.padding(start = 20.dp)) {
                TextField(
                    value = taskStructure.taskTitle, onValueChange = {
                        taskStructure.taskTitle = it
                    },
                    label = {
                        Text(
                            text = "Название задачи",
                        )
                    }, modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(CardBackGroundColor),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                )
            }
            Image(
                painterResource(id = R.drawable.ic_submit), modifier = Modifier
                    .size(64.dp)
                    .fillMaxWidth(0.2f)
                    .clickable {

                        if (taskStructure.taskTitle.filter { !it.isWhitespace() } != "") {
                            val task = TaskDb(
                                null,
                                taskStructure.taskTitle,
                                taskStructure.taskDesc,
                                taskStructure.taskDate
                            )
                            Thread {
                                db
                                    .getDao()
                                    .insertTask(task)
                            }.start()
                            navController.navigate("screen_diary") {
                                launchSingleTop = true
                                restoreState = true
                            }
                            taskStructure.taskTitle = ""
                            taskStructure.taskDesc = ""
                        }

                        navController.navigate("screen_addTask") {
                            launchSingleTop = true
                            restoreState = true

                        }
                    }, contentDescription = "submitImage"
            )
        }
        Card(modifier = Modifier.padding(20.dp), shape = RoundedCornerShape(20.dp)) {
            TextField(
                value = taskStructure.taskDesc, onValueChange = {
                    taskStructure.taskDesc = it
                }, shape = RoundedCornerShape(20.dp),
                label = {
                    Text(
                        text = "Описание",
                    )
                }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .defaultMinSize(minHeight = 200.dp)
                    .background(CardBackGroundColor)
                    .padding(15.dp),


                colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
            )
        }
        Column {
            val context = LocalContext.current

            val calendar = Calendar.getInstance()
            calendar.time = Date()

            val nowDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val nowMonth: Int = calendar.get(Calendar.MONTH)
            val nowYear: Int = calendar.get(Calendar.YEAR)
            val date = remember {
                mutableStateOf("$nowDay/${nowMonth + 1}/$nowYear")
            }
            val datePickerDialog = remember {
                val dialog = DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        date.value = "$dayOfMonth/${month + 1}/$year"
                        calendar.set(year, month, dayOfMonth)
                        taskStructure.taskDate = calendar.time
                    },
                    nowYear,
                    nowMonth,
                    nowDay
                )
                dialog.datePicker.minDate = Date().time
                dialog
            }
            Text(text = "Выбранная дата: ${date.value}")
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = {
                datePickerDialog.show()


            }) {
                Text(text = "Выбрать дату")
            }

        }


    }

}