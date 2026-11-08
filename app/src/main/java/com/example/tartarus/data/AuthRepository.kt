package com.example.tartarus.data

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class AuthRepository {

    suspend fun signUp(email: String, password: String, username: String) {
        SupabaseModule.client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = buildJsonObject {
                put("username", JsonPrimitive(username))
            }
        }
    }

    suspend fun signIn(email: String, password: String) {
        SupabaseModule.client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        try {
            withTimeout(5_000) {
                SupabaseModule.client.auth.signOut()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            SupabaseModule.client.auth.clearSession()
        }
    }

    fun currentUserEmail(): String? =
        SupabaseModule.client.auth.currentUserOrNull()?.email

    fun currentUsername(): String? {
        val user = SupabaseModule.client.auth.currentUserOrNull() ?: return null
        return user.userMetadata?.get("username")?.toString()
            ?: user.email?.substringBefore("@")
            ?: "Пользователь"
    }

    fun currentUserId(): String? =
        SupabaseModule.client.auth.currentUserOrNull()?.id


    fun isLoggedIn(): Boolean =
        SupabaseModule.client.auth.currentUserOrNull() != null

    suspend fun getGameHistory(): List<GameHistory> {
        val userId = currentUserId() ?: return emptyList()

        val records = SupabaseModule.client.postgrest["Game"]
            .select {
                filter { eq("User_id", userId) }
                order("Game_id", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
            }
            .decodeList<GameRecord>()

        return records.groupBy { it.sessionId }.map { (sessionId, sessionRecords) ->
            GameHistory(
                sessionId = sessionId,
                score = sessionRecords.sumOf { it.score },
                date = "Игра #$sessionId",
                correctAnswers = sessionRecords.count { it.isCorrect },
                totalQuestions = sessionRecords.size
            )
        }
    }
}