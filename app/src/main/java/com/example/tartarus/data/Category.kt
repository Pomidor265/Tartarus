package com.example.tartarus.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(

    @SerialName("Category_id") val id: Long,
    @SerialName("Name") val name: String
)