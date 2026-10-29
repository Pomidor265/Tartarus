package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Mark
import com.example.tartarus.data.MarkRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel : ViewModel() {
    private val repo = MarkRep()

    private val _bookmarks = MutableStateFlow<List<Mark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadBookmarks()
    }

    fun loadBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _bookmarks.value = repo.getMyMarks()
            } catch (e: Exception) {
                // log
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createBookmark(name: String) {
        viewModelScope.launch {
            try {
                repo.createMark(name)
                loadBookmarks() // Обновляем список
            } catch (e: Exception) {
                // log
            }
        }
    }

    fun deleteBookmark(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteBookmark(id)
                loadBookmarks()
            } catch (e: Exception) {
                // log
            }
        }
    }
}