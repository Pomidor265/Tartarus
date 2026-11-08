package com.example.tartarus.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.ViewModel.QuizViewModel
import com.example.tartarus.data.Difficulty

@Composable
fun QuizScreen(
    difficulty: Difficulty,
    onQuizFinished: () -> Unit,
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: QuizViewModel = viewModel()
) {
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentIndex.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    val isGameFinished by viewModel.isGameFinished.collectAsStateWithLifecycle()
    val currentScore by viewModel.currentScore.collectAsStateWithLifecycle()
    val answersHistory by viewModel.answersHistory.collectAsStateWithLifecycle()

    var userAnswer by remember { mutableStateOf("") }

    LaunchedEffect(difficulty) {
        viewModel.loadQuestions(difficulty)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isGameFinished) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Игра окончена!",
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF2A1A0F)
                )

                Text(
                    text = "Ваш результат: $currentScore очков",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                HorizontalDivider(color = Color.Black, thickness = 1.dp)
                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(answersHistory) { result ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = result.questionText,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = "Ваш ответ: ${result.userAnswer}",
                                    fontFamily = FontFamily.Serif,
                                    color = if (result.isCorrect) Color(0xFF1BC015) else Color(0xFFC62828)
                                )

                                if (!result.isCorrect) {
                                    Text(
                                        text = "Правильный ответ: ${result.correctAnswer}",
                                        fontFamily = FontFamily.Serif,
                                        color = Color(0xFF1BC015)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onQuizFinished,
                    modifier = Modifier.width(200.dp).height(50.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                ) {
                    Text("Назад", fontSize = 18.sp, fontFamily = FontFamily.Serif)
                }
            }
        } else if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF8B0000))
        } else if (questions.isEmpty()) {
            Text(
                "Вопросы не найдены",
                modifier = Modifier.align(Alignment.Center),
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp
            )
        } else {
            val currentQuestion = questions[currentIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 80.dp, end = 16.dp, top = 32.dp, bottom = 180.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Вопрос ${currentIndex + 1} из ${questions.size}",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF2A1A0F)
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Color.Black)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentQuestion.text,
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(16.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    placeholder = { Text("Введите ответ", fontFamily = FontFamily.Serif) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (userAnswer.isBlank()) return@Button
                        viewModel.submitAnswer(userAnswer, onQuizFinished)
                        userAnswer = ""
                    },
                    enabled = userAnswer.isNotBlank(),
                    modifier = Modifier.width(200.dp).height(50.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                ) {
                    Text("Подтвердить", fontSize = 18.sp, fontFamily = FontFamily.Serif)
                }
            }
        }

        if (!isGameFinished) {
            Box(modifier = Modifier.align(Alignment.TopStart)) {
                SideBar(
                    onHomeClick = onHomeClick,
                    onBookmarkClick = onBookmarkClick,
                    onGameClick = {},
                    onProfileClick = onProfileClick
                )
            }

            Image(
                painter = painterResource(R.drawable.sealing),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(170.dp)
            )
        }
    }
}