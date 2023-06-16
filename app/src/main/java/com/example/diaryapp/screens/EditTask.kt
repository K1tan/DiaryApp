package com.example.diaryapp.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.ContextProvider
import com.example.diaryapp.R
import com.example.diaryapp.TaskStructure
import com.example.diaryapp.TaskViewModel
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    navController: NavHostController,
    taskStructure: TaskStructure,
    taskId: Int?
) {
    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    var showConfirmationDialog by remember { mutableStateOf(false) }
    val taskViewModel: TaskViewModel = viewModel()
    val dbTask by taskViewModel.task.collectAsState()
    val newCheckboxes = mutableListOf<Boolean>()
    val newCheckboxesText = mutableListOf<String>()
    val checkboxes: MutableList<Boolean> = remember {
        mutableStateListOf()
    }
    val checkboxesText: MutableList<String> = remember {
        mutableStateListOf()
    }
    val repeatOptions = RepeatOption.values().toList()
    var repeatMenuExpanded by remember { mutableStateOf(false) }
    newCheckboxes.clear()
    newCheckboxesText.clear()
    LaunchedEffect(Unit) {
        if (taskId != null) {
            taskViewModel.getTaskById(db, taskId)
        }
    }
    LaunchedEffect(dbTask) {
        dbTask?.let { task ->
            taskStructure.taskTitle = task.taskTitle
            taskStructure.taskDesc = task.taskDesc
            taskStructure.taskDate = task.date
            taskStructure.taskRepeatOption = task.repeatOption
            taskStructure.taskTime = task.time
            taskStructure.checkboxes.clear()
            taskStructure.checkboxesText.clear()
            checkboxes.addAll(task.checkboxes)
            checkboxesText.addAll(task.checkboxesText)

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(end = 16.dp, start = 16.dp, bottom = 64.dp),

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
                    value = taskStructure.taskTitle, onValueChange = {
                        taskStructure.taskTitle = it
                    },
                    label = {
                        Text(
                            text = "Название задачи",
                            color = textColor
                        )
                    }, modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(cardBackground),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = textColor,
                        containerColor = Color.Transparent
                    )
                )
            }
            Image(
                painterResource(
                    id = R.drawable.ic_submit),

                modifier = Modifier
                    .size(48.dp)
                    .fillMaxWidth(0.2f)
                    .clickable {

                        if (taskStructure.taskTitle.filter { !it.isWhitespace() } != "") {
                            //taskStructure.taskTime = selectedTime.value
                            //taskStructure.taskRepeatOption = selectedRepeatOption.value
                            checkboxes.forEachIndexed { index, _ ->
                                if (checkboxesText[index].isNotBlank()) {
                                    newCheckboxes.add(checkboxes[index])
                                    newCheckboxesText.add(checkboxesText[index])

                                }
                            }
                            taskStructure.checkboxes.addAll(newCheckboxes)
                            taskStructure.checkboxesText.addAll(newCheckboxesText)
                            dbTask?.let { task ->
                                val updatedTask = task.copy(
                                    taskTitle = taskStructure.taskTitle,
                                    taskDesc = taskStructure.taskDesc,
                                    date = taskStructure.taskDate,
                                    time = taskStructure.taskTime,
                                    repeatOption = taskStructure.taskRepeatOption,
                                    checkboxes = taskStructure.checkboxes,
                                    checkboxesText = taskStructure.checkboxesText
                                )
                                taskViewModel.viewModelScope.launch {
                                    taskViewModel.updateTask(db, updatedTask)
                                }
                            }
                            navController.navigate("screen_addTask") {
                                launchSingleTop = true
                                restoreState = true

                            }

                            taskStructure.taskTitle = ""
                            taskStructure.taskDesc = ""
                            checkboxes.clear()
                            checkboxesText.clear()
                        } else Toast
                            .makeText(
                                ContextProvider.getContext(),
                                "Пожалуйста введите название задачи.",
                                Toast.LENGTH_SHORT
                            )
                            .show()


                    }, contentDescription = "submitImage"
            )
        }
        Card(shape = RoundedCornerShape(8.dp)) {
            TextField(
                value = taskStructure.taskDesc, onValueChange = {
                    taskStructure.taskDesc = it
                }, shape = RoundedCornerShape(20.dp),
                label = {
                    Text(
                        text = "Описание",
                        color = textColor
                    )
                }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .defaultMinSize(minHeight = 50.dp)
                    .background(cardBackground)
                    .padding(8.dp),


                colors = TextFieldDefaults.textFieldColors(
                    textColor = textColor,
                    containerColor = Color.Transparent
                )
            )
        }

        Column {
            checkboxes.forEachIndexed { index, isChecked ->
                Log.d("MyTag", "in foreach ${checkboxesText[index]}")
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 3.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            checkboxes[index] = checked
                            //taskStructure.checkboxes.set(index,checked)

                        },
                        colors = CheckboxDefaults.colors(GreenSoft)
                    )
                    OutlinedTextField(
                        value = checkboxesText[index],
                        onValueChange = { newValue ->
                            checkboxesText[index] = newValue
                            //taskStructure.checkboxesText.set(index,newValue)


                        },
                        shape = RoundedCornerShape(20.dp),
                        label = {
                            Text(text = "Название", color = textColor)
                        },
                        modifier = Modifier
                            //.fillMaxWidth(1f)
                            .defaultMinSize(minWidth = 10.dp, minHeight = 30.dp)
                            .background(Color.Transparent)
                            .padding(5.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            containerColor = cardBackground
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(GreenSoft),
                    onClick = {
                        checkboxes.add(false)
                        checkboxesText.add("")

                    }
                ) {
                    Text(text = "Добавить чекбокс", color = Color.Black)
                }
            }
        }
        Card(
            modifier = Modifier.padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Повтор: ${taskStructure.taskRepeatOption.title}",
                    fontSize = 16.sp,
                    color = textColor
                )
                Box(modifier = Modifier.padding(start = 20.dp)) {
                    DropdownMenu(
                        expanded = repeatMenuExpanded,
                        onDismissRequest = { repeatMenuExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        repeatOptions.forEach { option ->
                            val selected = option == taskStructure.taskRepeatOption
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = GreenSoft
                                        )
                                    )
                                    Text(
                                        text = option.title,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 4.dp),
                                        fontSize = 16.sp
                                    )
                                }
                            }, onClick = {
                                taskStructure.taskRepeatOption = option

                            })
                        }
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(GreenSoft),
                        onClick = { repeatMenuExpanded = !repeatMenuExpanded },
                    ) {
                        Text(
                            text = "Изменить",
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            val context = LocalContext.current
            val currentTime = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            val time = remember {
                mutableStateOf("${timeFormat.format(taskStructure.taskTime.time.time)}")
            }
            val timePickerDialog = remember {
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        taskStructure.taskTime.apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }
                        Log.d("develop", "pick: ${hourOfDay}, $minute")
                        time.value = "$hourOfDay:$minute"
                    },
                    currentTime.get(Calendar.HOUR_OF_DAY),
                    currentTime.get(Calendar.MINUTE),

                    true
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Log.d("develop", "ts: ${taskStructure.taskTime.time.time}")
                val formattedTime =
                Text(text = "Напоминание: ${time.value}", fontSize = 16.sp, color = textColor)
                Button(
                    colors = ButtonDefaults.buttonColors(GreenSoft),
                    onClick = {
                        timePickerDialog.show()
                    }
                ) {
                    Text(text = "Изменить", color = Color.Black)
                }
            }
        }

        Column {
            val context = LocalContext.current

            val calendar = Calendar.getInstance()
//            calendar.time = taskStructure.taskDate

            val nowDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val nowMonth: Int = calendar.get(Calendar.MONTH)
            val nowYear: Int = calendar.get(Calendar.YEAR)
            val date = remember {
                mutableStateOf("$nowYear-${nowMonth + 1}-$nowDay")
            }
            val datePickerDialog = remember {
                val dialog = DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        date.value = "$year-${month + 1}-$dayOfMonth"
                        calendar.set(year, month, dayOfMonth)
                        taskStructure.taskDate = date.value
                    },
                    nowYear,
                    nowMonth,
                    nowDay
                )
                dialog.datePicker.minDate = Date().time
                dialog
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBackground,
                    contentColor = Color.White
                ),
                // shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal =  8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Дата: ${date.value}", fontSize = 16.sp, color = textColor)
                    Button(
                        colors = ButtonDefaults.buttonColors(GreenSoft),
                        onClick = {
                            datePickerDialog.show()
                        }
                    ) {
                        Text(text = "Изменить", color = Color.Black)
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (showConfirmationDialog) {
                    AlertDialog(
                        modifier = Modifier.background(Color.Transparent),
                        onDismissRequest = { showConfirmationDialog = false },
                        title = {
                            Text(
                                text = "Подтвердите действие",
                                color = textColor
                            )
                        },
                        text = {
                            Text(
                                text = "Вы уверены, что хотите безвозвратно удалить задачу?",
                                color = textColor
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    navController.navigate("screen_addTask") {
                                        launchSingleTop = true
                                        restoreState = true

                                    }
                                    taskId?.let {
                                        taskViewModel.deleteTaskById(
                                            db,
                                            it
                                        )
                                    }
                                    taskStructure.taskTitle = ""
                                    taskStructure.taskDesc = ""
                                    checkboxes.clear()
                                    checkboxesText.clear()
                                    showConfirmationDialog = false
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
                Button(
                    onClick = { showConfirmationDialog = true },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(text = "Удалить", color = textColor)
                }
            }


        }


    }
}

@Preview
@Composable
fun MyViewPreview() {
    EditTaskScreen(
        navController = NavHostController(LocalContext.current),
        taskStructure = TaskStructure(),
        taskId = 1
    )
}
