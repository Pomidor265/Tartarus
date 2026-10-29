package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mark(
    @SerialName("Mark_id") val id: Long?,
    @SerialName("Mark_name") val name: String,
    @SerialName("User_id") val userId: String,
)