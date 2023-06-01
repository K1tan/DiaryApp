package com.example.diaryapp.other

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date

@Composable
fun ShowDateTimePicker(){
    val context = LocalContext.current

    val year: Int
    val month: Int
    val day :Int



    val calendar = Calendar.getInstance()
    year=calendar.get(Calendar.YEAR)
    month=calendar.get(Calendar.MONTH)
    day=calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time= Date()

    var pickedDay:Int = calendar.get(Calendar.DAY_OF_MONTH)
    var pickedMonth:Int = calendar.get(Calendar.MONTH)
    var pickedYear:Int = calendar.get(Calendar.YEAR)

    val date = remember {
        mutableStateOf("$day/$month/$year")
    }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.value = "$dayOfMonth/${month+1}/$year"
                pickedDay = dayOfMonth
                pickedMonth = month
                pickedYear = year
            },
            year,
            month,
            day
        )
    }
    Column(
    ) {

        Text(text = "Selected Date: ${date.value}")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text(text = "Open Date Picker")
        }
    }
    calendar.set(pickedYear, pickedMonth, pickedDay)

}