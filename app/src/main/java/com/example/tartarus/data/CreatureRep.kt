package com.example.tartarus.data

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest

class CreatureRep {
    private val table = SupabaseModule.client.postgrest["Creatures"]

    suspend fun getCreaturesByCategory(categoryId: Long): List<Creature> {
        Log.d("CreatureRep", "Запрос существ для категории $categoryId")
        return table.select {
            filter { eq("Category_id", categoryId) }
        }.decodeList<Creature>()
    }

    suspend fun getCreatureById(creatureId: Long): Creature? {
        return table.select {
            filter { eq("Creature_id", creatureId) }
        }.decodeSingleOrNull<Creature>()
    }
}