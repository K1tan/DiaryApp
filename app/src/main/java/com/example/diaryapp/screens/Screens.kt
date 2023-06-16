package com.example.diaryapp.screens


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.diaryapp.ContextProvider
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.database.MainDb
import com.example.diaryapp.date.DateInRussianFormat
import com.example.diaryapp.ui.theme.AwfulMoodColor
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BackGroundColorLight
import com.example.diaryapp.ui.theme.BadMoodColor
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.CardBackGroundColorLight
import com.example.diaryapp.ui.theme.GoodMoodColor
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.NormalMoodColor
import com.example.diaryapp.ui.theme.SuperMoodColor
import com.example.diaryapp.ui.theme.TextColorDark
import com.example.diaryapp.ui.theme.TextColorLight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.*
import java.util.Calendar
import java.util.Date


lateinit var noteTitle: MutableState<String>
lateinit var noteText: MutableState<String>


val db = MainDb.getDb(ContextProvider.getContext())

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DiaryScreen(navController: NavHostController) {
    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor =
        if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground =
        if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight

    val noteViewModel: NoteViewModel = viewModel()
    val dbNotes by noteViewModel.notes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    val filteredNotes by noteViewModel.filteredNotes.collectAsState()

    val permissionState = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DatePickerButton(selectedDate = selectedDate) { date ->
                selectedDate = date
                noteViewModel.searchNotes(searchQuery, selectedDate)
            }

            TextField(
                colors = TextFieldDefaults.textFieldColors(cardBackground),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    noteViewModel.searchNotes(searchQuery, selectedDate)
                },
                label = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            painterResource(id = R.drawable.search_icon),
                            contentDescription = "ic_search",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(text = "ПОИСК", fontSize = 14.sp)
                    }
                },
                shape = RoundedCornerShape(30.dp),
            )
        }


        Box(Modifier.padding(bottom = 50.dp)) {

            LaunchedEffect(Unit) {
                if (!permissionState.status.isGranted) {
                    permissionState.launchPermissionRequest()
                }
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
                            color = textColor,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    items(
                        filteredNotes
                    ) { note ->
                        Box {
                            var showConfirmationDialog by remember { mutableStateOf(false) }
                            var expanded by remember { mutableStateOf(false) }
                            //подвердить удаление:
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
                                            text = "Вы уверены, что хотите безвозвратно удалить запись?",
                                            color = textColor
                                        )
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                note.id?.let {
                                                    noteViewModel.deleteNoteById(
                                                        db,
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
                                    backgroundColor = cardBackground,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
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
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        when (note.noteMood) {
                                            R.drawable.ic_awful -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .background(
                                                            AwfulMoodColor,
                                                            shape = CircleShape
                                                        )
                                                ) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_awful),
                                                        contentDescription = "ic_awful",
                                                        Modifier.size(48.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_bad -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .background(
                                                            BadMoodColor,
                                                            shape = CircleShape
                                                        )
                                                ) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_bad),
                                                        contentDescription = "ic_bad",
                                                        Modifier.size(48.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_normal -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .background(
                                                            NormalMoodColor,
                                                            shape = CircleShape
                                                        )
                                                ) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_normal),
                                                        contentDescription = "ic_normal",
                                                        Modifier.size(48.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_good -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .background(
                                                            GoodMoodColor,
                                                            shape = CircleShape
                                                        )
                                                ) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_good),
                                                        contentDescription = "ic_good",
                                                        Modifier.size(48.dp)
                                                    )
                                                }
                                            }

                                            R.drawable.ic_super -> {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .background(
                                                            SuperMoodColor,
                                                            shape = CircleShape
                                                        )
                                                ) {
                                                    Image(
                                                        painterResource(id = R.drawable.ic_super),
                                                        contentDescription = "ic_super",
                                                        Modifier.size(48.dp)
                                                    )
                                                }
                                            }

                                            else -> Box(
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .background(
                                                        NormalMoodColor,
                                                        shape = CircleShape
                                                    )
                                            ) {
                                                Image(
                                                    painterResource(id = R.drawable.ic_normal),
                                                    contentDescription = "ic_normal",
                                                    Modifier.size(48.dp)
                                                )
                                            }
                                        }

                                        Row {
                                            Column {
                                                Text(
                                                    //modifier = Modifier.fillMaxWidth(),
                                                    text = "${DateInRussianFormat(date = note.noteDate)}",
                                                    fontSize = 20.sp,
                                                    color = textColor
                                                )
                                                /**ACTIVITY**/
                                                for (activity in note.activityIds) {
                                                    Text(
                                                        text = "• ${activity.name}",
                                                        fontSize = 16.sp,
                                                        color = textColor,
                                                        modifier = Modifier.padding(top = 4.dp)
                                                    )
                                                }
                                            }
                                            Box {            //меню редактировать/удалить
                                                IconButton(onClick = { expanded = true }) {
                                                    Icon(
                                                        Icons.Default.MoreVert,
                                                        contentDescription = "Показать меню"
                                                    )
                                                }
                                                DropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    modifier = Modifier
                                                        .wrapContentWidth()
                                                        .align(Alignment.Center)
                                                ) {
                                                    DropdownMenuItem(onClick = {
                                                        val noteId = note.id ?: -1

                                                        expanded = false
                                                        navController.navigate(
                                                            route = "screen_editDataNote/$noteId",
                                                            builder = {
                                                                launchSingleTop = true
                                                                restoreState = true
                                                            })
                                                    }) {
                                                        Text(text = "Редактировать")
                                                    }
                                                    Divider()
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
                                            modifier = Modifier.fillMaxWidth(1f),
                                            text = note.noteTitle,
                                            fontSize = 24.sp,
                                            color = textColor
                                        )
                                    }

                                    Text(
                                        text = note.noteText,
                                        fontSize = 20.sp,
                                        color = textColor,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                                    )

                                    if (note.photoUrl != null) {

                                        if (permissionState.status.isGranted) {

                                            Row(
                                                Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                AsyncImage(
                                                    model = note.photoUrl,
                                                    contentDescription = "",
                                                    modifier = Modifier.size(200.dp),
                                                    contentScale = ContentScale.Crop
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
}


@Composable
fun DatePickerButton(selectedDate: Date?, onDateSelected: (Date) -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            showDatePickerDialog(context, selectedDate) { date ->
                onDateSelected(date)
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.5f)

            .background(Color.Transparent),
        colors = ButtonDefaults.buttonColors(CardBackGroundColor)
    ) {
        Text(text = "КАЛЕНДАРЬ", color = Color.White, modifier = Modifier.padding(vertical = 10.dp))
    }
}


fun showDatePickerDialog(context: Context, initialDate: Date?, onDateSelected: (Date) -> Unit) {
    val calendar = Calendar.getInstance()
    initialDate?.let { calendar.time = it }

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val selectedDate = calendar.time
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePicker.show()
}