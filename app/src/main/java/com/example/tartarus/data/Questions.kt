package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    @SerialName("Questions_id") val id: Long,
    @SerialName("Difficulty_id") val difficultyId: Long,
    @SerialName("Creature_id") val creatureId: Long? = null,
    @SerialName("Question_text") val text: String,
    @SerialName("Correct_answer") val correctAnswer: String,
    @SerialName("Score_earned") val scoreEarned: Int
)