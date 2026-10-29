package com.example.tartarus.data

import io.github.jan.supabase.postgrest.postgrest

class CategoryRep {
    private val table = SupabaseModule.client.postgrest["Category"]

    suspend fun getCategories(): List<Category> = table.select().decodeList<Category>()
}