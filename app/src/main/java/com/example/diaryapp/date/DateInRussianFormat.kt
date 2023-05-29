package com.example.diaryapp.date

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateInRussianFormat(date: Date):String {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    val formattedDate = dateFormat.format(date)

    return formattedDate
}

/*@Preview
@Composable
fun PreviewDateInRussianFormat() {
    DateInRussianFormat(Date())
}
 */