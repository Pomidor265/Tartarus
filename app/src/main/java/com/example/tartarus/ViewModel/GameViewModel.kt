package com.example.tartarus.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Difficulty
import com.example.tartarus.data.GameRep
import com.example.tartarus.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val repo = GameRep()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var sessionId: Long = 0

    fun startGame(difficulty: Difficulty) {
        viewModelScope.launch {
            _isLoading.value = true
            sessionId = Random.nextLong(100000, 999999)
            try {
                _questions.value = repo.generateQuizQuestions(difficulty)
                _currentIndex.value = 0
            } catch (e: Exception) {
                // log
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(answer: String, onQuizFinished: () -> Unit) {
        val currentQ = _questions.value.getOrNull(_currentIndex.value) ?: return

        viewModelScope.launch {
            try {
                // Сохраняем в БД
                repo.saveAnswer(currentQ, answer, sessionId)

                // Переходим к следующему
                if (_currentIndex.value < _questions.value.size - 1) {
                    _currentIndex.value += 1
                } else {
                    onQuizFinished() // Конец игры
                }
            } catch (e: Exception) {
                // log
            }
        }
    }
}