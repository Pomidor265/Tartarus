package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkCreature(
    @SerialName("Id") val id: Long? = null,
    @SerialName("Mark_id") val markId: Long?,
    @SerialName("Creature_id") val creatureId: Long
)