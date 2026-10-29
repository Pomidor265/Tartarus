package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Creature
import com.example.tartarus.data.MarkRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarkDetailViewModel : ViewModel() {
    private val repo = MarkRep()

    private val _creatures = MutableStateFlow<List<Creature>>(emptyList())
    val creatures = _creatures.asStateFlow()

    fun loadCreatures(bookmarkId: Long) {
        viewModelScope.launch {
            try {
                _creatures.value = repo.getCreaturesInBookmark(bookmarkId)
            } catch (e: Exception) { }
        }
    }
}