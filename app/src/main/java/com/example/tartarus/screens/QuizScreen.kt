package com.example.tartarus.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tartarus.R
import com.example.tartarus.SideBar
import com.example.tartarus.data.Difficulty
import com.example.tartarus.data.GameRep
import com.example.tartarus.data.Question
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun QuizScreen(
    difficulty: Difficulty,
    onQuizFinished: () -> Unit,
    onHomeClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val repo = remember { GameRep() }
    val scope = rememberCoroutineScope()

    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // Добавим состояние для показа ошибки на экране
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val sessionId = remember { Random.nextLong(100000, 999999) }

    LaunchedEffect(difficulty) {
        try {
            questions = repo.generateQuizQuestions(difficulty)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("QuizScreen", "Ошибка загрузки вопросов", e)
            errorMessage = "Ошибка загрузки: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isLoading) {
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

                // Блок с текстом вопроса
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

                // Показываем ошибку, если она есть
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Поле ввода
                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    placeholder = { Text("Введите ответ", fontFamily = FontFamily.Serif) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Кнопка
                Button(
                    onClick = {
                        if (userAnswer.isBlank()) return@Button

                        scope.launch {
                            isSaving = true
                            errorMessage = null
                            try {
                                Log.d("QuizScreen", "Попытка сохранить ответ: $userAnswer")
                                val success = repo.saveAnswer(currentQuestion, userAnswer, sessionId)
                                Log.d("QuizScreen", "Ответ сохранен. Правильно: $success")

                                if (currentIndex < questions.size - 1) {
                                    currentIndex++
                                    userAnswer = ""
                                } else {
                                    onQuizFinished()
                                }
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                Log.e("QuizScreen", "Ошибка сохранения ответа", e)
                                // ВЫВОДИМ ОШИБКУ НА ЭКРАН
                                errorMessage = "Ошибка базы: ${e.message}"
                            } finally {
                                isSaving = false
                            }
                        }
                    },
                    enabled = !isSaving && userAnswer.isNotBlank(),
                    modifier = Modifier.width(200.dp).height(50.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Подтвердить", fontSize = 18.sp, fontFamily = FontFamily.Serif)
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            SideBar(
                onHomeClick = onHomeClick,
                onBookmarkClick = onBookmarkClick,
                onGameClick = { },
                onProfileClick = onProfileClick
            )
        }
    }
}