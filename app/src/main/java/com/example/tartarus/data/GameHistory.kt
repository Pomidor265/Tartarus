package com.example.tartarus.data

data class GameHistory(
    val sessionId: Long,
    val score: Int,
    val date: String,
    val correctAnswers: Int,
    val totalQuestions: Int
)