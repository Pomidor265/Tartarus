package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameRecord(
    @SerialName("Game_id") val gameId: Long,
    @SerialName("User_id") val userId: String,
    @SerialName("Session_id") val sessionId: Long,
    @SerialName("Score") val score: Int,
    @SerialName("Is_correct") val isCorrect: Boolean,
    @SerialName("User_answer") val userAnswer: String
)