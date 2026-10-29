package com.example.tartarus

import android.util.Log
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tartarus.data.Creature
import com.example.tartarus.data.CreatureRep
import com.example.tartarus.data.Mark
import com.example.tartarus.data.MarkRep
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun CreatureDetailScreen(
    creatureId: Long,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onExtraClick: () -> Boolean
) {
    val repo = remember { CreatureRep() }
    val bookmarkRepo = remember { MarkRep() }
    val scope = rememberCoroutineScope()

    var creature by remember { mutableStateOf<Creature?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    var showBookmarkDialog by remember { mutableStateOf(false) }
    var bookmarkList by remember { mutableStateOf<List<Mark>>(emptyList()) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(creatureId) {
        try {
            creature = repo.getCreatureById(creatureId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CreatureDetail", "Ошибка", e)
            error = e.message
        }
    }

    LaunchedEffect(showBookmarkDialog) {
        if (showBookmarkDialog) {
            try {
                bookmarkList = bookmarkRepo.getMyMarks()
            } catch (e: Exception) {
                bookmarkList = emptyList()
            }
        }
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

            Spacer(Modifier.height(24.dp))

            when {
                error != null -> Text("Ошибка: $error", color = Color(0xFF8B0000))
                creature == null -> Text("Загрузка...", color = Color(0xFF2A1A0F))
                else -> {
                    Text(
                        text = creature!!.name,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF2A1A0F),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    if (!creature!!.image.isNullOrBlank()) {
                        AsyncImage(
                            model = creature!!.image,
                            contentDescription = creature!!.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Text(
                        text = creature!!.description ?: "",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF2A1A0F),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { showBookmarkDialog = true },
                        modifier = Modifier.width(220.dp).height(48.dp)
                    ) {
                        Text("В закладки", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick
            )
        }

        Image(
            painter = painterResource(R.drawable.sealing),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(180.dp)
        )
    }

    if (showBookmarkDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSaving) showBookmarkDialog = false },
            title = { Text("Выберите закладку") },
            text = {
                if (bookmarkList.isEmpty()) {
                    Text("Нет закладок. Создайте их на экране «Метки».")
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                        items(bookmarkList) { bm ->
                            Text(
                                text = bm.name,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !isSaving) {
                                        scope.launch {
                                            isSaving = true
                                            try {
                                                bookmarkRepo.addCreatureToMark(bm.id, creatureId)
                                                showBookmarkDialog = false
                                            } catch (e: Exception) {
                                                Log.e("CreatureDetail", "Не добавилось", e)
                                            } finally {
                                                isSaving = false
                                            }
                                        }
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBookmarkDialog = false }, enabled = !isSaving) {
                    Text("Закрыть")
                }
            }
        )
    }
}