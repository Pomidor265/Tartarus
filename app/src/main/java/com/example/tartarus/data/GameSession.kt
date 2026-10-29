package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameSession(
    @SerialName("User_id") val userId: String,
    @SerialName("Question_id") val questionId: Long,
    @SerialName("User_answer") val userAnswer: String,
    @SerialName("Is_correct") val isCorrect: Boolean,
    @SerialName("Score") val score: Int,
    @SerialName("Session_id") val sessionId: Long
)