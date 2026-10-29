package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Difficulty(
    @SerialName("Difficulty_id") val id: Long,
    @SerialName("Name") val name: String,
    @SerialName("Number_of_questions") val questionsCount: Int
)