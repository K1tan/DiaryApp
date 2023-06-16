package com.example.diaryapp.screens

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diaryapp.ContextProvider
import com.example.diaryapp.NoteStructure
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.database.NoteDb
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
import com.pixplicity.easyprefs.library.Prefs

@Composable
fun AddNoteScreen(
    navController: NavHostController,
    noteStructure: NoteStructure,
) {

    val photoUrl = remember { mutableStateOf("") }
    //val selectedImageId = rememberSaveable { mutableStateOf(R.drawable.ic_super) }
    val noteViewModel: NoteViewModel = viewModel()
    val activities by noteViewModel.activities.collectAsState()

    val textColor = if (Prefs.getBoolean("darkTheme", false)) TextColorDark else TextColorLight
    val backgroundColor = if (Prefs.getBoolean("darkTheme", false)) BackGroundColor else BackGroundColorLight
    val cardBackground = if (Prefs.getBoolean("darkTheme", false)) CardBackGroundColor else CardBackGroundColorLight


    val selectedPhoto: MutableState<Uri?> = remember { mutableStateOf(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Обработка выбранной фотографии
        if (uri != null) {
            val newPhotoUrl = uri.toString()
            noteStructure.notePhoto = newPhotoUrl
            photoUrl.value = newPhotoUrl
            selectedPhoto.value = uri
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        println("selected file URI ${it.data?.data}")
        val uri = it.data?.data
        if (uri != null) {
            val newPhotoUrl = uri.toString()
            noteStructure.notePhoto = newPhotoUrl
            photoUrl.value = newPhotoUrl
            selectedPhoto.value = uri
        }
    }

    LaunchedEffect(Unit) {
        noteViewModel.loadActivities()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
            .verticalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Как вы себя чувствуете?", color = textColor, fontSize = 20.sp)
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(cardBackground)

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
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.7f),

                color = textColor,
                fontSize = 20.sp
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
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(cardBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(GreenSoft),
                                modifier = Modifier
                                    .padding(end = 2.dp)
                            )

                            Text(text = activity.name, color = textColor, fontSize = 15.sp)
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
                color = textColor,
                fontSize = 20.sp
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
                .defaultMinSize(minHeight = 130.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(cardBackground)

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
                    Text(text = noteStructure.noteTitle, fontSize = 30.sp, color = textColor)
                }

                Text(
                    text = noteStructure.noteText,
                    fontSize = 20.sp,
                    color = textColor,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = {
                // Открыть галерею для выбора фотографии


                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    .apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }
                launcher.launch(intent)
//                galleryLauncher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(GreenSoft)
        ) {
            Text(text = "Добавить фото", color = Color.Black, fontSize = 20.sp)
        }
        Log.d("dev", "selecP: ${selectedPhoto.value}")

        if (selectedPhoto.value != null) {
            Log.d("dev", "true")
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedPhoto.value)
                    .build(),
                contentDescription = "icon",
                contentScale = ContentScale.Inside,
                modifier = Modifier.size(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        noteStructure.notePhoto,
                        noteStructure.selectedActivities
                    )
                    Thread {
                        db.getDao().insertNote(note)
                    }.start()

                    noteStructure.noteTitle = ""
                    noteStructure.noteText = ""
                    noteStructure.noteMood = R.drawable.ic_normal
                    noteStructure.notePhoto = ""
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
