package com.example.diaryapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.core.app.ActivityScenario.launch
import com.example.diaryapp.NoteViewModel
import com.example.diaryapp.R
import com.example.diaryapp.database.NoteDb
import com.example.diaryapp.other.ActivityMentionItem
import com.example.diaryapp.ui.theme.AwfulMoodColor
import com.example.diaryapp.ui.theme.BackGroundColor
import com.example.diaryapp.ui.theme.BadMoodColor
import com.example.diaryapp.ui.theme.CardBackGroundColor
import com.example.diaryapp.ui.theme.GoodMoodColor
import com.example.diaryapp.ui.theme.NormalMoodColor
import com.example.diaryapp.ui.theme.SuperMoodColor

@Composable
fun StatScreen() {
    val noteViewModel: NoteViewModel = viewModel()
    val dbNotes by noteViewModel.notes.collectAsState()
    val activities by noteViewModel.activities.collectAsState()
    val activityMentions by noteViewModel.countActivityMentions().collectAsState(emptyMap())
    var moodCountList = remember { mutableListOf(0,0,0,0,0) }
    moodCountList = countMoods(dbNotes)

    activityMentions.forEach{ activity->
        Log.d("MyTag","$activity")
    }
    LaunchedEffect(Unit) {
        noteViewModel.getAllNotes(db)



    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(bottom = 70.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(ScrollState(0))
        ) {
            Text(text = "Статистика", color = Color.White, fontSize = 30.sp)
            Divider(modifier = Modifier.padding(vertical = 20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(CardBackGroundColor)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Счетчик настроений",
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    SuperMoodColor,
                                    shape = CircleShape
                                )
                        ) {
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
                        Text(
                            text = "${moodCountList[0]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    GoodMoodColor,
                                    shape = CircleShape
                                )
                        ) {
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
                        Text(
                            text = "${moodCountList[1]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    NormalMoodColor,
                                    shape = CircleShape
                                )
                        ) {
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
                        Text(
                            text = "${moodCountList[2]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    BadMoodColor,
                                    shape = CircleShape
                                )
                        ) {
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
                        Text(
                            text = "${moodCountList[3]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    AwfulMoodColor,
                                    shape = CircleShape
                                )
                        ) {
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
                        Text(
                            text = "${moodCountList[4]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(CardBackGroundColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Счетчик занятий",
                            color = Color.White,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 20.dp,top = 10.dp)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),

                    ) {
                        val activityMentionList = activityMentions.toList()

                        items(activityMentionList.chunked(3)) { chunkedList ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                chunkedList.forEach { (activity, mentionCount) ->
                                    ActivityMentionItem(activity.name, mentionCount)
                                }
                                repeat(4 - chunkedList.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }


                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(CardBackGroundColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Часто вместе с...",
                            color = Color.White,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                        Text(
                            text = "С такими настроениями вы чаще всего отмечаете эти занятия",
                            color = Color.White,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Divider()
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {

                        Column() {
                            Row(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            SuperMoodColor,
                                            shape = CircleShape
                                        )

                                ) {
                                    Image(
                                        painterResource(id = R.drawable.ic_super),
                                        contentDescription = "superMood",
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                                Row() {
                                    //Text(text = "ACTIVITY NAME : ACTIVITYMENTION")

                                }
                            }
                            Row(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            GoodMoodColor,
                                            shape = CircleShape
                                        )

                                ) {
                                    Image(
                                        painterResource(id = R.drawable.ic_good),
                                        contentDescription = "superMood",
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                            Row(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            NormalMoodColor,
                                            shape = CircleShape
                                        )

                                ) {
                                    Image(
                                        painterResource(id = R.drawable.ic_normal),
                                        contentDescription = "superMood",
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                            Row(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            BadMoodColor,
                                            shape = CircleShape
                                        )

                                ) {
                                    Image(
                                        painterResource(id = R.drawable.ic_bad),
                                        contentDescription = "superMood",
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                            Row(modifier = Modifier.padding(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            AwfulMoodColor,
                                            shape = CircleShape
                                        )

                                ) {
                                    Image(
                                        painterResource(id = R.drawable.ic_awful),
                                        contentDescription = "superMood",
                                        modifier = Modifier.size(64.dp)
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

fun countMoods(notes: List<NoteDb>): MutableList<Int> {
    val moodCount = mutableListOf<Int>(0,0,0,0,0)

    for (note in notes) {
        val moodIndex = convertMoodToIndex(note.noteMood)
        if (moodIndex != -1) {
            moodCount[moodIndex]++
        }
    }

    return moodCount
}

fun convertMoodToIndex(mood: Int): Int {
    return when (mood) {
        R.drawable.ic_super -> 0 //super
        R.drawable.ic_good -> 1 //good
        R.drawable.ic_normal -> 2 //normal
        R.drawable.ic_bad -> 3 //bad
        R.drawable.ic_awful -> 4 //awful
        else -> -1 // Invalid mood
    }
}

