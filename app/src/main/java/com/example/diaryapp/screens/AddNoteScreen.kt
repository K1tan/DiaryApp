package com.example.diaryapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.diaryapp.database.ActivitiesDb
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.ui.theme.AwfulMoodColor
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BadMoodColor
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.GoodMoodColor
import com.example.diaryapp.ui.theme.GreenSoft
import com.example.diaryapp.ui.theme.NormalMoodColor
import com.example.diaryapp.ui.theme.SuperMoodColor

@Composable
fun AddNoteScreen(navController: NavHostController, noteStructure: NoteStructure) {
    //val selectedImageId = rememberSaveable { mutableStateOf(R.drawable.ic_super) }
    val noteViewModel: NoteViewModel = viewModel()
    val activities by noteViewModel.activities.collectAsState()

    LaunchedEffect(Unit) {
        noteViewModel.loadActivities()
    }

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Чем вы занимались?",
                modifier = Modifier.padding(top = 10.dp),

                color = Color.White,
                fontSize = 30.sp
            )
            IconButton(

                onClick = {
                    navController.navigate("screen_addActivity") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(top = 10.dp),
                //modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Icon(
                    painterResource(id = R.drawable.add_note_icon),
                    contentDescription = "Добавить занятие",
                    tint = Color.White
                )
            }
        }

        Card(
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(CardBackGroundColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(start = 10.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(100.dp)
                ) {
                    items(activities.size) { index ->
                        val activity = activities[index]
                        val isChecked = noteStructure.selectedActivities.contains(activity)

                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    noteStructure.selectedActivities = if (isChecked) {
                                        noteStructure.selectedActivities - activity
                                    } else {
                                        noteStructure.selectedActivities + activity
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(GreenSoft),
                                modifier = Modifier
                                    .padding(end = 5.dp)
                            )

                            Text(text = activity.name, color = Color.White)
                        }
                    }
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
                .defaultMinSize(minHeight = 170.dp),
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
                if (noteStructure.noteTitle.filter { !it.isWhitespace() } != "") {
                    Text(text = noteStructure.noteTitle, fontSize = 30.sp, color = Color.White)
                }

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
                .padding(top = 10.dp, bottom = 50.dp)
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
                        noteStructure.noteDate,
                        noteStructure.selectedActivities
                    )
                    Thread {
                        db.getDao().insertNote(note)
                    }.start()


                    noteStructure.noteTitle = ""
                    noteStructure.noteText = ""
                    noteStructure.noteMood = R.drawable.ic_normal
                    noteStructure.selectedActivities = emptyList()

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
