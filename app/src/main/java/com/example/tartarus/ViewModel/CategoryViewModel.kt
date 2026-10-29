package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Category
import com.example.tartarus.data.CategoryRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CategoryState {
    object Loading : CategoryState
    data class Success(val categories: List<Category>) : CategoryState
    data class Error(val message: String) : CategoryState
}

class CategoryViewModel : ViewModel() {
    private val repo = CategoryRep()

    private val _state = MutableStateFlow<CategoryState>(CategoryState.Loading)
    val state = _state.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = CategoryState.Loading
            try {
                val cats = repo.getCategories()
                _state.value = CategoryState.Success(cats)
            } catch (e: Exception) {
                _state.value = CategoryState.Error(e.message ?: "Ошибка")
            }
        }
    }
}