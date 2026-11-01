package com.example.tartarus.data


import android.util.Log
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

class GameRep {

    private val questionsTable = SupabaseModule.client.postgrest["Questions"]
    private val gameTable = SupabaseModule.client.postgrest["Game"]

    suspend fun getDifficulties(): List<Difficulty> {
        return SupabaseModule.client.postgrest["Difficulty"]
            .select()
            .decodeList<Difficulty>()
    }

    suspend fun generateQuizQuestions(difficulty: Difficulty): List<Question> {
        val targetCount = difficulty.questionsCount

        val primaryQuestions = questionsTable.select {
            filter { eq("Difficulty_id", difficulty.id) }
        }.decodeList<Question>()

        val selected = primaryQuestions.shuffled().take(targetCount).toMutableList()

        if (selected.size < targetCount) {
            val needed = targetCount - selected.size
            val fallbackQuestions = questionsTable.select {
                filter { neq("Difficulty_id", difficulty.id) }
            }.decodeList<Question>()

            val additional = fallbackQuestions.shuffled().take(needed)
            selected.addAll(additional)
        }

        return selected.shuffled()
    }

    suspend fun saveAnswer(
        question: Question,
        userAnswer: String,
        sessionId: Long
    ): Boolean {
        val userId = SupabaseModule.client.auth.currentUserOrNull()?.id ?: return false

        val isCorrect = userAnswer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
        val score = if (isCorrect) question.scoreEarned else 0

        val session = GameSession(
            userId = userId,
            questionId = question.id,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            score = score,
            sessionId = sessionId
        )

        gameTable.insert(session)
        return isCorrect
    }
}