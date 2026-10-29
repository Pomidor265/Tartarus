package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Difficulty
import com.example.tartarus.data.GameRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DifficultyViewModel : ViewModel() {
    private val repo = GameRep()

    private val _difficulties = MutableStateFlow<List<Difficulty>>(emptyList())
    val difficulties = _difficulties.asStateFlow()

    init { loadDifficulties() }

    fun loadDifficulties() {
        viewModelScope.launch {
            try {
                _difficulties.value = repo.getDifficulties()
            } catch (e: Exception) { }
        }
    }
}