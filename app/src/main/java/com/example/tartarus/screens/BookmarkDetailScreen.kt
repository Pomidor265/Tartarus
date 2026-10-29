package com.example.tartarus.screens

import com.example.tartarus.data.MarkRep
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tartarus.R
import com.example.tartarus.SideBar

import com.example.tartarus.data.Creature
import kotlinx.coroutines.CancellationException

@Composable
fun BookmarkDetailScreen(
    bookmarkId: Long,
    bookmarkName: String,
    onCreatureClick: (Creature) -> Unit = {},
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val repo = remember { MarkRep() }
    var creatures by remember { mutableStateOf<List<Creature>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(bookmarkId) {
        try {
            creatures = repo.getCreaturesInBookmark(bookmarkId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("BookmarkDetail", "Ошибка", e)
            error = e.message
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 80.dp, end = 16.dp, top = 32.dp, bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "← Назад",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF8B0000),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onBackClick() }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = bookmarkName,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            when {
                error != null -> Text("Ошибка: $error", color = Color(0xFF8B0000))
                creatures.isEmpty() -> Text(
                    "Пусто",
                    color = Color(0xFF2A1A0F),
                    fontFamily = FontFamily.Serif
                )
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(creatures) { creature ->
                        Text(
                            text = creature.name,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF2A1A0F),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCreatureClick(creature) }
                                .padding(vertical = 8.dp)
                        )
                        Divider(
                            color = Color(0xFF2A1A0F),
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBackClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick
            )
        }
    }
}