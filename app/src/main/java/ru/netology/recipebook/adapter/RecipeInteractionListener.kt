package ru.netology.recipebook.adapter

import ru.netology.recipebook.dto.Recipe

interface RecipeInteractionListener {


    fun onAddFavoriteClicked(recipe: Recipe)
    fun removeClickListener(recipe: Recipe)
    fun editClickListener(recipe: Recipe)
    fun onRecipeClicked(recipe: Recipe)
    //fun shareClickListener(recipe: Recipe)


}