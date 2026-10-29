package com.example.tartarus.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.Category
import com.example.tartarus.data.Creature
import com.example.tartarus.data.CreatureRep
import com.example.tartarus.data.SupabaseModule
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreaturesViewModel : ViewModel() {
    private val repo = CreatureRep()

    private val _categoryName = MutableStateFlow("")
    val categoryName = _categoryName.asStateFlow()

    private val _creatures = MutableStateFlow<List<Creature>>(emptyList())
    val creatures = _creatures.asStateFlow()

    fun loadData(categoryId: Long) {
        viewModelScope.launch {
            try {
                // Имя категории
                try {
                    _categoryName.value = SupabaseModule.client
                        .postgrest["Category"]
                        .select { filter { eq("Category_id", categoryId) } }
                        .decodeSingleOrNull<Category>()
                        ?.name ?: "Категория $categoryId"
                } catch (e: Exception) { }

                // Существа
                _creatures.value = repo.getCreaturesByCategory(categoryId)
            } catch (e: Exception) { }
        }
    }
}