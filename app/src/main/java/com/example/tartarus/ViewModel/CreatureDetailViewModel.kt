package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Creature
import com.example.tartarus.data.CreatureRep
import com.example.tartarus.data.Mark
import com.example.tartarus.data.MarkRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatureDetailViewModel : ViewModel() {
    private val creatureRepo = CreatureRep()
    private val markRepo = MarkRep()

    private val _creature = MutableStateFlow<Creature?>(null)
    val creature = _creature.asStateFlow()

    private val _bookmarks = MutableStateFlow<List<Mark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()

    fun loadCreature(id: Long) {
        viewModelScope.launch {
            try {
                _creature.value = creatureRepo.getCreatureById(id)
            } catch (e: Exception) { }
        }
    }

    fun loadBookmarks() {
        viewModelScope.launch {
            try {
                _bookmarks.value = markRepo.getMyMarks()
            } catch (e: Exception) { }
        }
    }

    fun addToBookmark(bookmarkId: Long?, creatureId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                markRepo.addCreatureToMark(bookmarkId, creatureId)
                onSuccess()
            } catch (e: Exception) { }
        }
    }
}