package com.example.tartarus.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
import com.example.tartarus.data.AuthRepository
import com.example.tartarus.data.GameHistory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onGoToLogin: () -> Unit = {},
    onHomeClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    val repo = remember { AuthRepository() }

    LaunchedEffect(Unit) {
        if (!repo.isLoggedIn()) {
            onGoToLogin()
        }
    }

    val username = repo.currentUsername()
    val email = repo.currentUserEmail()

    // Состояния для истории
    var history by remember { mutableStateOf<List<GameHistory>>(emptyList()) }
    var isLoadingHistory by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            history = repo.getGameHistory()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("ProfileScreen", "Ошибка загрузки истории", e)
        } finally {
            isLoadingHistory = false
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
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

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
                text = "Личный кабинет",
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = username ?: "Гость",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F)
            )

            if (email != null) {
                Text(
                    text = email,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF5A4A3A)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Блок с историей
            Text(
                text = "История игр",
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2A1A0F),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(8.dp))

            if (isLoadingHistory) {
                CircularProgressIndicator(color = Color(0xFF8B0000), modifier = Modifier.size(32.dp))
            } else if (history.isEmpty()) {
                Text(
                    text = "Вы ещё не играли",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { game ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = game.date,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF2A1A0F)
                            )
                            Text(
                                text = "${game.score} очков",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF1565C0)
                            )
                        }
                        Divider(color = Color(0xFF2A1A0F).copy(alpha = 0.3f))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    onGoToLogin()
                    GlobalScope.launch {
                        try {
                            repo.signOut()
                        } catch (e: Exception) {
                            Log.e("ProfileScreen", "Ошибка выхода", e)
                        }
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
            ) {
                Text("Выйти", fontFamily = FontFamily.Serif)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}