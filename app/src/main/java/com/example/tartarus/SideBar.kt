package com.example.tartarus

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideBar(
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    showExtra: Boolean = false,
    extraIcon: Int = R.drawable.back,
    extraLabel: String = "Назад",
    onExtraClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxHeight(0.40f)
            .width(72.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.sidebar),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuIcon(R.drawable.home,     "Главная", onHomeClick)
            MenuIcon(R.drawable.bookmark, "Метки",   onBookmarkClick)
            MenuIcon(R.drawable.game,     "Игра",    onGameClick)
            MenuIcon(R.drawable.me,       "Я",       onProfileClick)
            if (showExtra){
                MenuIcon(extraIcon, extraLabel, onExtraClick)
            }
        }
    }
}

@Composable
private fun MenuIcon(icon: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = label,
            fontFamily = FontFamily.Serif,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}