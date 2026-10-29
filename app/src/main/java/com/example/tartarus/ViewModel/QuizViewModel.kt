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

class QuizViewModel : ViewModel() {
    private val repo = GameRep()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var sessionId: Long = Random.nextLong(100000, 999999)

    fun loadQuestions(difficulty: Difficulty) {
        if (_questions.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _questions.value = repo.generateQuizQuestions(difficulty)
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(answer: String, onFinished: () -> Unit) {
        val currentQ = _questions.value.getOrNull(_currentIndex.value) ?: return

        viewModelScope.launch {
            try {
                repo.saveAnswer(currentQ, answer, sessionId)
                if (_currentIndex.value < _questions.value.size - 1) {
                    _currentIndex.value += 1
                } else {
                    onFinished()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка базы: ${e.message}"
            }
        }
    }
}