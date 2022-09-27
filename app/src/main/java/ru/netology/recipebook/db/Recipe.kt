package ru.netology.recipebook.db

import ru.netology.recipebook.dto.Recipe
import ru.netology.recipebook.dto.Step

internal fun RecipeWithSteps.toRecipe() = Recipe(
    id = recipe.id,
    author = recipe.author,
    title = recipe.title,
    recipeCategory = recipe.recipeCategory,
    content = step.map {
        it.toStep()
    },
    isFavorite = recipe.isFavorite,
    indexPosition = recipe.indexNumber
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    author = author,
    title = title,
    recipeCategory = recipeCategory,
    isFavorite = isFavorite,
    indexNumber = indexPosition
)

internal fun StepEntity.toStep() = Step(
    idStep = idStep,
    idRecipe = idRecipe,
    stepText = stepText,
    picture = picture
)

internal fun Step.toEntity() = StepEntity(
    idStep = idStep,
    idRecipe = idRecipe,
    stepText = stepText,
    picture = picture
)