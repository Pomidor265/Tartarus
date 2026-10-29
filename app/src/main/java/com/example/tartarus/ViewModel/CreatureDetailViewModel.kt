package com.example.tartarus.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Mark
import com.example.tartarus.data.MarkRep
import com.example.tartarus.data.Creature
import com.example.tartarus.data.CreatureRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatureDetailViewModel : ViewModel() {
    private val creatureRepo = CreatureRep()
    private val bookmarkRepo = MarkRep()

    private val _creature = MutableStateFlow<Creature?>(null)
    val creature = _creature.asStateFlow()

    private val _bookmarks = MutableStateFlow<List<Mark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun loadCreature(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _creature.value = creatureRepo.getCreatureById(id)
            } catch (e: Exception) {
                // log
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBookmarksForDialog() {
        viewModelScope.launch {
            try {
                _bookmarks.value = bookmarkRepo.getMyMarks()
            } catch (e: Exception) {
                // log
            }
        }
    }

    fun saveToBookmark(bookmarkId: Long, creatureId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                bookmarkRepo.addCreatureToMark(bookmarkId, creatureId)
                onSuccess()
            } catch (e: Exception) {
                // log
            }
        }
    }
}