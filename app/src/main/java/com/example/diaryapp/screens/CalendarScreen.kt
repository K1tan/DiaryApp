package com.example.diaryapp.screens

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.R
import com.example.diaryapp.TaskViewModel
import com.example.diaryapp.database.TaskDb
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun CalendarScreen(navController: NavHostController) {
    val taskViewModel: TaskViewModel = viewModel()
    val dbTasks by taskViewModel.tasks.collectAsState()
    LaunchedEffect(Unit) {
        taskViewModel.getAllTasks(db)
    }
    val filteredTasks by taskViewModel.filteredTasks.collectAsState()
    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight


    val dateFormatPattern = "yyyy-MM-dd"
    val dateFormatter = SimpleDateFormat(dateFormatPattern, Locale.getDefault())

    var clickedCalendarElem by remember {
        mutableStateOf<CalendarInput?>(null)
    }
    var currentMonth by remember {
        mutableStateOf(Calendar.getInstance().get(Calendar.MONTH))
    }
    LaunchedEffect(Unit) {
        taskViewModel.getAllTasks(db)
    }

    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var selectedDate by remember { mutableStateOf(0) }

    val calendarInputs = mutableListOf<CalendarInput>()
    for (i in 1..getDaysInMonth(currentMonth, currentYear)) {
        val date = "${currentYear}-${currentMonth + 1}-$i"
        Log.d("develop", "qwerDate: $date")
        val tasksForDay = dbTasks.filter { it.date == date }
        calendarInputs.add(CalendarInput(i, tasksForDay))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                currentMonth -= 1
                if (currentMonth < 0) {
                    currentMonth = 11
                    currentYear -= 1
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = "Previous Month",
                    tint = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${
                        if (selectedDate != 0) {
                            selectedDate.toString()
                        } else {
                            ""
                        }
                    } " + "${getMonthName(currentMonth)} $currentYear",
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    fontSize = 25.sp
                )

            }
            IconButton(onClick = {
                currentMonth += 1
                if (currentMonth > 11) {
                    currentMonth = 0
                    currentYear += 1
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Next Month",
                    tint = Color.White
                )
            }
        }
        val daysInMonth = getDaysInMonth(currentMonth, currentYear)
        Calendar(
            calendarInput = calendarInputs.take(daysInMonth),
            onDayClick = { day ->
                clickedCalendarElem = calendarInputs.first { it.day == day }
                selectedDate = day
            },
            //month = getMonthName(currentMonth),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(1.3f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            for (calendarInput in calendarInputs) {
                val isSelectedDay = selectedDate == calendarInput.day
                if (isSelectedDay) {
                    Log.d("develop", "day: ${calendarInput.tasks}")
                    Text(
                        text = "День ${calendarInput.day}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = textColor
                    )

//                    taskViewModel.getTasksForDate()

                    if (calendarInput.tasks.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.padding(bottom = 56.dp)) {
                            items(items = calendarInput.tasks) {
                                ItemView(
                                    item = it,
                                    onClick = { task ->
                                        val taskId = task.id ?: -1
                                        navController.navigate(
                                            route = "screen_editTask/$taskId",
                                            builder = {
                                                launchSingleTop = true
                                                restoreState = true
                                            })
                                    }
                                )
                            }
                        }
                    } else {
                        Text(text = "Нет задач на сегодня", color = textColor)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(item: TaskDb, onClick: (TaskDb) -> Unit) {
    Column(
        modifier = Modifier
            .clickable {
                onClick.invoke(item)
            }
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = item.taskTitle,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = if (item.taskDesc.isNotEmpty()) "Описание: ${item.taskDesc}" else "Нет описания задачи",
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Повтор: ${item.repeatOption.title}",
            style = TextStyle(fontSize = 14.sp)
        )
    }
}


private const val CALENDAR_ROWS = 5
private const val CALENDAR_COLUMNS = 7

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarInput: List<CalendarInput>,
    onDayClick: (Int) -> Unit,
    strokeWidth: Float = 15f,
    //month:String
) {

    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }
    var clickAnimationOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var animationRadius by remember {
        mutableStateOf(0f)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset ->
                            val column =
                                (offset.x / canvasSize.width * CALENDAR_COLUMNS).toInt() + 1
                            val row = (offset.y / canvasSize.height * CALENDAR_ROWS).toInt() + 1
                            val day = column + (row - 1) * CALENDAR_COLUMNS
                            if (day <= calendarInput.size) {
                                onDayClick(day)
                                clickAnimationOffset = offset
                                scope.launch {
                                    animate(0f, 225f, animationSpec = tween(300)) { value, _ ->
                                        animationRadius = value
                                    }
                                }
                            }

                        }
                    )
                }
        ) {
            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)
            val ySteps = canvasHeight / CALENDAR_ROWS
            val xSteps = canvasWidth / CALENDAR_COLUMNS

            val column = (clickAnimationOffset.x / canvasSize.width * CALENDAR_COLUMNS).toInt() + 1
            val row = (clickAnimationOffset.y / canvasSize.height * CALENDAR_ROWS).toInt() + 1

            val path = Path().apply {
                moveTo((column - 1) * xSteps, (row - 1) * ySteps)
                lineTo(column * xSteps, (row - 1) * ySteps)
                lineTo(column * xSteps, row * ySteps)
                lineTo((column - 1) * xSteps, row * ySteps)
                close()
            }

            clipPath(path) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(GreenSoft.copy(0.8f), GreenSoft.copy(0.2f)),
                        center = clickAnimationOffset,
                        radius = animationRadius + 0.1f
                    ),
                    radius = animationRadius + 0.1f,
                    center = clickAnimationOffset
                )
            }

            drawRoundRect(
                GreenSoft,
                cornerRadius = CornerRadius(25f, 25f),
                style = Stroke(
                    width = strokeWidth
                )
            )

            for (i in 1 until CALENDAR_ROWS) {
                drawLine(
                    color = GreenSoft,
                    start = Offset(0f, ySteps * i),
                    end = Offset(canvasWidth, ySteps * i),
                    strokeWidth = strokeWidth
                )
            }
            for (i in 1 until CALENDAR_COLUMNS) {
                drawLine(
                    color = GreenSoft,
                    start = Offset(xSteps * i, 0f),
                    end = Offset(xSteps * i, canvasHeight),
                    strokeWidth = strokeWidth
                )
            }
            val textHeight = 16.dp.toPx()
            for (i in calendarInput.indices) {
                val textPositionX = xSteps * (i % CALENDAR_COLUMNS) + strokeWidth
                val textPositionY = (i / CALENDAR_COLUMNS) * ySteps + textHeight + strokeWidth / 2
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${i + 1}",
                        textPositionX,
                        textPositionY,
                        Paint().apply {
                            textSize = textHeight
                            color = Color.Green.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }

}


private fun getMonthName(month: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month)
    return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
}

private fun getDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

data class TaskEntry(val title: String, val description: String)
data class CalendarInput(val day: Int, val tasks: List<TaskDb>)
