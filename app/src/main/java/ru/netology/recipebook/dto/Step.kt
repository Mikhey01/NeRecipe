package ru.netology.recipebook.dto

import kotlinx.serialization.Serializable

@Serializable
data class Step(
    val idStep: Long,
    val idRecipe: Long,
    val stepText: String,
    var picture: String = ""
)