package com.example.diaryapp.bottom_nav

import android.app.DatePickerDialog
import kotlinx.coroutines.*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.diaryapp.ContextProvider

import com.example.diaryapp.NoteStructure
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.TaskStructure
import com.example.diaryapp.TaskViewModel
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.database.TaskDb
import com.example.diaryapp.date.DateInRussianFormat
import com.example.diaryapp.other.ShowDateTimePicker
import com.example.diaryapp.ui.theme.AwfulMoodColor
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BadMoodColor
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.GoodMoodColor
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.NormalMoodColor
import com.example.diaryapp.ui.theme.SuperMoodColor
import java.util.Calendar
import java.util.Date


lateinit var noteTitle: MutableState<String>
lateinit var noteText: MutableState<String>


val db = MainDb.getDb(ContextProvider.getContext())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen() {
    var filterText = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "(КАЛЕНДАРЬ)", fontSize = 35.sp, color = Color.White)
            TextField(
                colors = TextFieldDefaults.textFieldColors(CardBackGroundColor),
                value = filterText.value,
                onValueChange = { filterText.value = it },
                label = {
                    Row() {
                        Image(
                            painterResource(id = R.drawable.search_icon),
                            contentDescription = "ic_search",
                            modifier = Modifier.size(32.dp)
                        )
                        Text(text = "ПОИСК", fontSize = 15.sp)
                    }
                },
                shape = RoundedCornerShape(30.dp),
            )
        }



        Box(Modifier.padding(bottom = 50.dp)) {
            val noteViewModel: NoteViewModel = viewModel()
            val dbNotes by noteViewModel.notes.collectAsState()

            LaunchedEffect(Unit) {
                noteViewModel.getAllNotes(db)
            }
            LazyColumn {
                if (dbNotes.isEmpty()) {
                    item {
                        Text(
                            text = "Здесь пусто, создайте запись",
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    items(dbNotes) { note ->
                        Box() {
                            var showConfirmationDialog by remember { mutableStateOf(false) }
                            var expanded by remember { mutableStateOf(false) }
                            if (showConfirmationDialog) {
                                AlertDialog(
                                    modifier = Modifier.background(Color.Gray),
                                    onDismissRequest = { showConfirmationDialog = false },
                                    title = { Text(text = "Подтвердите действие", color = Color.White) },
                                    text = { Text(text = "Вы уверены, что хотите безвозвратно удалить запись?", color = Color.White) },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                note.id?.let {
                                                    noteViewModel.deleteNoteById(db,
                                                        it
                                                    )
                                                }
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
                                    backgroundColor = CardBackGroundColor,
                                    shape = RoundedCornerShape(40.dp)
                                )
                            }
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(30.dp),
                                colors = CardDefaults.cardColors(CardBackGroundColor)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        when (note.noteMood) {
                                            R.drawable.ic_awful -> {
                                                Box(modifier = Modifier
                                                    .size(64.dp)
                                                    .background(AwfulMoodColor, shape = CircleShape)) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_awful),
                                                        contentDescription = "ic_awful",
                                                        Modifier.size(64.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_bad -> {
                                                Box(modifier = Modifier
                                                    .size(64.dp)
                                                    .background(BadMoodColor, shape = CircleShape)) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_bad),
                                                        contentDescription = "ic_bad",
                                                        Modifier.size(64.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_normal -> {
                                                Box(modifier = Modifier
                                                    .size(64.dp)
                                                    .background(
                                                        NormalMoodColor,
                                                        shape = CircleShape
                                                    )) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_normal),
                                                        contentDescription = "ic_normal",
                                                        Modifier.size(64.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_good -> {
                                                Box(modifier = Modifier
                                                    .size(64.dp)
                                                    .background(GoodMoodColor, shape = CircleShape)) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_good),
                                                        contentDescription = "ic_good",
                                                        Modifier.size(64.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_super -> {
                                                Box(modifier = Modifier
                                                    .size(64.dp)
                                                    .background(SuperMoodColor, shape = CircleShape)) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_super),
                                                        contentDescription = "ic_super",
                                                        Modifier.size(64.dp)
                                                    )
                                                }
                                            }

                                            else -> Box(modifier = Modifier
                                                .size(64.dp)
                                                .background(NormalMoodColor, shape = CircleShape)) {
                                                Image(
                                                    painterResource(id = R.drawable.ic_normal),
                                                    contentDescription = "ic_normal",
                                                    Modifier.size(64.dp)
                                                )
                                            }
                                        }

                                        Row() {
                                            Column() {
                                                Text(
                                                    //modifier = Modifier.fillMaxWidth(),
                                                    text = "${DateInRussianFormat(date = note.noteDate)}",
                                                    fontSize = 20.sp,
                                                    color = Color.White
                                                )
                                                Text(
                                                    //modifier = Modifier.fillMaxWidth(0.5f),
                                                    text = note.noteActivity,
                                                    fontSize = 20.sp,
                                                    color = Color.White
                                                )
                                            }
                                            Box{            //меню редактировать/удалить
                                                IconButton(onClick = { expanded = true }) {
                                                    Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
                                                }
                                                DropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    modifier = Modifier.wrapContentWidth().align(Alignment.Center)
                                                ) {
                                                    DropdownMenuItem(onClick = {

                                                        expanded = false
                                                    }) {
                                                        Text(text = "Редактировать ${note.id}")
                                                    }
                                                    DropdownMenuItem(onClick = {
                                                        /*
                                                        note.id?.let {
                                                            noteViewModel.deleteNoteById(db,
                                                                it
                                                            )
                                                        }
                                                        **/
                                                        showConfirmationDialog = true

                                                        expanded = false

                                                    }) {
                                                        Text(text = "Удалить", color = Color.Red)
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    if (note.noteTitle.filter { !it.isWhitespace() } != "") {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            text = note.noteTitle,
                                            fontSize = 30.sp,
                                            color = Color.White
                                        )
                                    }

                                    Text(
                                        text = note.noteText,
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AddNoteScreen(navController: NavHostController, noteStructure: NoteStructure) {
    //val selectedImageId = rememberSaveable { mutableStateOf(R.drawable.ic_super) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(20.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Как вы себя чувствуете?", color = Color.White, fontSize = 30.sp)
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(CardBackGroundColor)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (noteStructure.noteMood == R.drawable.ic_super) SuperMoodColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { noteStructure.noteMood = R.drawable.ic_super }) {
                        Image(
                            painterResource(id = R.drawable.ic_super),
                            contentDescription = "superMood",
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Text(
                        text = "СУПЕР",
                        color = SuperMoodColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (noteStructure.noteMood == R.drawable.ic_good) GoodMoodColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { noteStructure.noteMood = R.drawable.ic_good }) {
                        Image(
                            painterResource(id = R.drawable.ic_good),
                            contentDescription = "goodMood",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Text(
                        text = "ХОРОШО",
                        color = GoodMoodColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (noteStructure.noteMood == R.drawable.ic_normal) NormalMoodColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { noteStructure.noteMood = R.drawable.ic_normal }) {
                        Image(
                            painterResource(id = R.drawable.ic_normal),
                            contentDescription = "normalMood",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Text(
                        text = "ТАК СЕБЕ",
                        color = NormalMoodColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (noteStructure.noteMood == R.drawable.ic_bad) BadMoodColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { noteStructure.noteMood = R.drawable.ic_bad }) {
                        Image(
                            painterResource(id = R.drawable.ic_bad),
                            contentDescription = "badMood",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Text(
                        text = "ПЛОХО",
                        color = BadMoodColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (noteStructure.noteMood == R.drawable.ic_awful) AwfulMoodColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { noteStructure.noteMood = R.drawable.ic_awful }) {
                        Image(
                            painterResource(id = R.drawable.ic_awful),
                            contentDescription = "awfulMood",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Text(
                        text = "УЖАСНО",
                        color = AwfulMoodColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = " Заметка:",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 30.sp
            )
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    navController.navigate("screen_addDataNote") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = ButtonDefaults.buttonColors(GreenSoft),
            ) {
                Text(text = "Добавить", color = Color.Black, fontSize = 20.sp)
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .defaultMinSize(minHeight = 300.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(CardBackGroundColor)

        ) {
            /*noteTitle = rememberSaveable{
                mutableStateOf("")
            }
            noteText = rememberSaveable{
                mutableStateOf("")
            }*/
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            ) {
                Text(text = noteStructure.noteTitle, fontSize = 30.sp, color = Color.White)
                Text(
                    text = noteStructure.noteText,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 50.dp)
        ) {
            Button(onClick = {
                if (noteStructure.noteText.filter { !it.isWhitespace() } != "") {

                    navController.navigate("screen_diary") {
                        launchSingleTop = true
                        restoreState = true
                    }
                    val note = NoteDb(
                        null,
                        noteStructure.noteTitle,
                        noteStructure.noteText,
                        noteStructure.noteMood,
                        noteStructure.noteActivity,
                        noteStructure.noteDate
                    )
                    Thread {
                        db.getDao().insertNote(note)
                    }.start()


                    noteStructure.noteTitle = ""
                    noteStructure.noteText = ""
                    noteStructure.noteMood = R.drawable.ic_normal

                } else Toast.makeText(
                    ContextProvider.getContext(),
                    "Пожалуйста введите текст заметки.",
                    Toast.LENGTH_SHORT
                ).show()

            }, colors = ButtonDefaults.buttonColors(GreenSoft)) {
                Text(text = "Создать", color = Color.Black, fontSize = 20.sp)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var searchTask: String = ""
            OutlinedTextField(
                value = searchTask, onValueChange = { newText -> searchTask = newText },
                label = {
                    Text(
                        text = "Поиск", textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .weight(1f)
                    .height(45.dp),
                shape = RoundedCornerShape(20.dp),
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
            Box() {
                val taskViewModel: TaskViewModel = viewModel()
                val dbTasks by taskViewModel.tasks.collectAsState()

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
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        items(dbTasks) { task ->
                            Box() {
                                Card(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = CardDefaults.cardColors(CardBackGroundColor)
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                                    ) {
                                        Text(

                                            text = "${task.taskTitle}",
                                            fontSize = 25.sp,
                                            color = Color.White,
                                            lineHeight = 35.sp
                                        )
                                        Text(
                                            text = "${task.taskDesc}",
                                            fontSize = 20.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                text = "срок: ${DateInRussianFormat(date = task.date)}",
                                                fontSize = 20.sp,
                                                color = Color.White,
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    bottom = 20.dp
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

@Composable
fun CalendarScreen() {
    Text(
        text = "Календарь с задачами", modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(), textAlign = TextAlign.Center
    )
}

@Composable
fun SettingsScreen() {
    Text(
        text = "Настройки", modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(), textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDataScreen(navController: NavHostController, noteStructure: NoteStructure) {
    Column() {
        Row(
            modifier = Modifier
                .background(BackGroundColor)
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
                        .background(CardBackGroundColor),
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
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
                .background(CardBackGroundColor)
                .padding(15.dp),

            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )

    }

}

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
            calendar.time= Date()

            var nowDay:Int = calendar.get(Calendar.DAY_OF_MONTH)
            var nowMonth:Int = calendar.get(Calendar.MONTH)
            var nowYear:Int = calendar.get(Calendar.YEAR)
            val date = remember {
                mutableStateOf("$nowDay/${nowMonth+1}/$nowYear")
            }
            val datePickerDialog = remember {
                val dialog=DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        date.value = "$dayOfMonth/${month+1}/$year"
                        calendar.set(year,month,dayOfMonth)
                        taskStructure.taskDate = calendar.time
                    },
                    nowYear,
                    nowMonth,
                    nowDay
                )
                dialog.datePicker.minDate=Date().time
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