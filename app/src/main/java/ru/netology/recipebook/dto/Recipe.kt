package ru.netology.recipebook.dto

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(

    val id: Long,
    val author: String,
    val title: String,
    val recipeCategory: String,
    val content: List<Step>?,
    val isFavorite: Boolean = false,
    val indexPosition: Long
)
