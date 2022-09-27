package ru.netology.recipebook.repository

import androidx.lifecycle.LiveData
import ru.netology.recipebook.dto.Recipe
import ru.netology.recipebook.dto.Step

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

   // fun shareById(recipeId: Long)

    fun getNextIndexId(): Long

    fun addFavorite(recipeId: Long)

    fun delete(recipeId: Long)

    fun save(recipe: Recipe)

    fun updateListOnMove(from: Long, to: Long, fromId: Long, toId: Long)

    fun deleteStep(step: Step)

    fun saveStep(step: Step)

    fun getFilteredList(
        filters: MutableSet<String>?
    ): LiveData<List<Recipe>>

    companion object {
        const val NEW_RECIPE_ID = 0L
        const val NEW_STEP_ID = 0L
    }
}