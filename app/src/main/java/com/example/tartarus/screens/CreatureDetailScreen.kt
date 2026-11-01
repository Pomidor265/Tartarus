package com.example.tartarus.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.ViewModel.CreatureDetailViewModel

@Composable
fun CreatureDetailScreen(
    creatureId: Long,
    onExtraClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: CreatureDetailViewModel = viewModel()
) {
    val creature by viewModel.creature.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    var showBookmarkDialog by remember { mutableStateOf(false) }

    LaunchedEffect(creatureId) {
        viewModel.loadCreature(creatureId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 80.dp, end = 16.dp, top = 80.dp, bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentCreature = creature
            if (currentCreature == null) {
                Text("Загрузка...", color = Color(0xFF2A1A0F), fontFamily = FontFamily.Serif)
            } else {
                Text(
                    text = currentCreature.name,
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF2A1A0F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                if (!currentCreature.image.isNullOrBlank()) {
                    AsyncImage(
                        model = currentCreature.image,
                        contentDescription = currentCreature.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    text = currentCreature.description ?: "",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF2A1A0F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 24.dp)
                .size(48.dp)
                .clickable {
                    viewModel.loadBookmarks()
                    showBookmarkDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "В закладки",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }


        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick,
                showExtra = true,
                extraIcon = R.drawable.back,
                extraLabel = "Назад",
                onExtraClick = onExtraClick
            )
        }

    }

    if (showBookmarkDialog) {
        AlertDialog(
            onDismissRequest = { showBookmarkDialog = false },
            title = { Text("Выберите закладку") },
            text = {
                if (bookmarks.isEmpty()) {
                    Text("Нет закладок. Создайте их на экране «Метки».")
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                        items(bookmarks) { bm ->
                            Text(
                                text = bm.name,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Serif,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.addToBookmark(bm.id, creatureId) {
                                            showBookmarkDialog = false
                                        }
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBookmarkDialog = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
}