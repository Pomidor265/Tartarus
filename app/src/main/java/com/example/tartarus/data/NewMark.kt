package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewMark(
    @SerialName("Mark_name") val name: String,
    @SerialName("User_id")  val userId: String
)