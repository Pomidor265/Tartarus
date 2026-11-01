package com.example.tartarus.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.ViewModel.AuthViewModel

@Composable
fun ProfileScreen(
    onGoToLogin: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onGameClick: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val username by viewModel.username.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    val isLoadingHistory by viewModel.isLoadingHistory.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!viewModel.isLoggedIn()) {
            onGoToLogin()
        } else {
            viewModel.loadProfileData()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(modifier = Modifier.fillMaxSize()) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = onGameClick,
                onProfileClick = {},
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))

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
                        text = email!!,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF5A4A3A)
                    )
                }

                Spacer(Modifier.height(24.dp))

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
                            HorizontalDivider(color = Color(0xFF2A1A0F).copy(alpha = 0.3f))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.logout(onGoToLogin) },
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                ) {
                    Text("Выйти", fontFamily = FontFamily.Serif)
                }

                Spacer(Modifier.height(32.dp))
            }
        }

        Image(
            painter = painterResource(R.drawable.sealing),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(250.dp)
        )
    }
}