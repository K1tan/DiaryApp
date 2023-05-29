package com.example.diaryapp.bottom_nav


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.diaryapp.R
import com.example.diaryapp.ui.theme.GreenSoft


@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomNavController(navController: NavController) {
    val listItems = listOf(
        BottomNavItem.DiaryScreen,
        BottomNavItem.StatScreen,
        BottomNavItem.AddNoteScreen,
        BottomNavItem.AddTaskScreen,
        BottomNavItem.CalendarScreen,
        BottomNavItem.SettingsScreen
    )
    val bgNavColor:Color = Color(R.color.greenSoft)
    BottomNavigation(
        backgroundColor = GreenSoft,


    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRout = backStackEntry?.destination?.route

        listItems.forEach { item ->
            BottomNavigationItem(
                selected = currentRout == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "Icon",
                        modifier = Modifier.size(30.dp),

                    )
                },
                /*label = {
                    var isSelected = item.route == currentRout

                        Text(

                            text = if(isSelected) item.title else "",
                            fontSize = 5.sp,
                            softWrap = false,

                            modifier = Modifier.padding(horizontal = 2.dp)
                        )

                },*/
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black
            )
        }
    }

}