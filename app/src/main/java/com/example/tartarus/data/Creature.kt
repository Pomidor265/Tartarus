package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Creature (
    @SerialName("Creature_id") val id: Long,
    @SerialName("Category_id") val categoryId: Long,
    @SerialName("Name") val name: String,
    @SerialName("Description") val description: String,
    @SerialName("Image") val image: String? = null
)