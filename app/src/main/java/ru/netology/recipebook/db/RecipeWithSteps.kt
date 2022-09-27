package ru.netology.recipebook.db

import androidx.room.Embedded
import androidx.room.Relation

class RecipeWithSteps(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idRecipe"
    )
    val step: List<StepEntity>
)