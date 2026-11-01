package com.example.tartarus.data

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.postgrest.postgrest


class MarkRep {
    private val mark_table = SupabaseModule.client.postgrest["Bookmark"]
    private val link_table = SupabaseModule.client.postgrest["Bookmark_Creatures"]

    suspend fun getMyMarks(): List<Mark>{
        val userId = SupabaseModule.client.auth.currentUserOrNull()?.id ?: return emptyList()
        return mark_table.select {
            filter { eq("User_id", userId) }
        }.decodeList<Mark>()
    }

    suspend fun createMark(name: String): Mark{
        val userId = SupabaseModule.client.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Не авторизован")

        val newMark = NewMark(name = name, userId = userId)

        return mark_table.insert(newMark) {
            select()
        }.decodeSingle<Mark>()
    }

    suspend fun addCreatureToMark(markId: Long?, creatureId: Long){
        link_table.insert(MarkCreature(markId = markId, creatureId = creatureId))
    }

    suspend fun getCreaturesInBookmark(markId: Long): List<Creature>{
        val links = link_table.select { filter { eq("Mark_id", markId) } }.decodeList<MarkCreature>()

        if(links.isEmpty())return emptyList()
        val ids = links.map { it.creatureId }
        return SupabaseModule.client.postgrest["Creatures"].select { filter { isIn("Creature_id", ids) } }.decodeList<Creature>()
    }

    suspend fun deleteBookmark(bookmarkId: Long) {
        SupabaseModule.client.postgrest["Bookmark_Creatures"]
            .delete {
                filter { eq("Mark_id", bookmarkId) }
            }

        SupabaseModule.client.postgrest["Bookmark"]
            .delete {
                filter { eq("Mark_id", bookmarkId) }
            }
    }

}