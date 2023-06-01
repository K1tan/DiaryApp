package com.example.diaryapp.date

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateInRussianFormat(date: Date): String {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    return dateFormat.format(date)
}

/*@Preview
@Composable
fun PreviewDateInRussianFormat() {
    DateInRussianFormat(Date())
}
 */