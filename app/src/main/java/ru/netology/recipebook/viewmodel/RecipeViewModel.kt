package ru.netology.recipebook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.recipebook.adapter.FilterInteractionListener
import ru.netology.recipebook.adapter.RecipeInteractionListener
import ru.netology.recipebook.adapter.StepInteractionListener
import ru.netology.recipebook.db.AppDb
import ru.netology.recipebook.dto.Recipe
import ru.netology.recipebook.dto.Step
import ru.netology.recipebook.repository.RecipeRepository
import ru.netology.recipebook.repository.RecipeRepositoryImpl
import ru.netology.recipebook.util.SingleLiveEvent
import java.util.*

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener, FilterInteractionListener,
    StepInteractionListener {

    // private val repository: FileRecipeRepository = FileRecipeRepository(application)
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )

    val data by repository::data

    private val filters = MutableLiveData<MutableSet<String>?>(mutableSetOf())

    var filterResult = Transformations.switchMap(filters) { filter ->
        repository.getFilteredList(filter)
    }
    private val currentRecipe = MutableLiveData<Recipe?>(null)
    val currentStep = MutableLiveData<Step?>(null)
    val currentImageStep = MutableLiveData<String>("")

    //val shareEvent = SingleLiveEvent<String>()
    val navigateToRecipeEditOrAddScreenEvent = SingleLiveEvent<Recipe>()
    val navigateToCurrentRecipeScreenEvent = SingleLiveEvent<Recipe>()
    val navigateToStepEditScreenEvent = SingleLiveEvent<Step>()
    val navigateToStepAddScreenEvent = SingleLiveEvent<String>()
    //val searchEvent = SingleLiveEvent<String>()

    fun onSaveButtonClicked(
        title: String,
        author: String,
        recipeCategory: String,
        content: List<Step>?

    ) {
        val recipeForSave = currentRecipe.value?.copy(
            title = title,
            author = author,
            recipeCategory = recipeCategory,
            content = content
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            author = author,
            content = content,
            title = title,
            recipeCategory = recipeCategory,
            indexPosition = repository.getNextIndexId()
        )
        repository.save(recipeForSave)
        currentRecipe.value = null
    }

    fun onSaveButtonStepClicked(textStep: String) {
        if (textStep.isBlank()) return
        val stepForSave = currentStep.value?.copy(
            stepText = textStep,
            picture = currentStep.value!!.picture
        ) ?: Step(
            idStep = RecipeRepository.NEW_STEP_ID,
            idRecipe = currentRecipe.value?.id ?: 0,
            stepText = textStep,
            picture = currentImageStep.value.toString()
        )
        repository.saveStep(stepForSave)
        currentStep.value = null
        currentRecipe.value = null
        currentImageStep.value = ""
    }

    fun onAddClicked() {
        navigateToRecipeEditOrAddScreenEvent.call()
    }

//    override fun searchClickListener(recipe: Recipe) {
//        searchEvent.value = recipe.recipeCategory
//    }

    fun onAddStepClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToStepAddScreenEvent.call()
    }

    fun updateListOnMove(from: Long, to: Long, fromId: Long, toId: Long) {
        repository.updateListOnMove(from, to, fromId, toId)
    }

    fun filterRecipeByFavorite(recipes: List<Recipe>): List<Recipe> {
        return recipes.filter { it.isFavorite }
    }

    fun filterSearch(charForSearch: CharSequence?): MutableList<Recipe> {
        val filterRecipes = mutableListOf<Recipe>()
        val recipes = filterResult.value
        if (charForSearch?.isBlank() == true) {
            if (recipes != null) {
                filterRecipes.addAll(recipes)
            }
        } else if (recipes != null) {
            for (recipe in recipes) {
                if (
                    recipe.title
                        .lowercase(Locale.getDefault())
                        .contains(
                            charForSearch.toString().lowercase(Locale.getDefault())
                        )
                ) {
                    filterRecipes.add(recipe)
                }
            }
        }
        return filterRecipes
    }

    override fun onAddFavoriteClicked(recipe: Recipe) {
        repository.addFavorite(recipe.id)
    }

//    override fun shareClickListener(recipe: Recipe) {
//        repository.shareById(recipe.id)
//        shareEvent.value = recipe.content.toString()
//    }

    override fun removeClickListener(recipe: Recipe) = repository.delete(recipe.id)
    override fun editClickListener(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeEditOrAddScreenEvent.value = recipe
    }

    override fun onRecipeClicked(recipe: Recipe) {
        navigateToCurrentRecipeScreenEvent.value = recipe
    }

    override fun checkboxFilterPressOn(recipeCategory: String) {
        val filterList = filters.value
        filterList?.add(recipeCategory)
        filters.value = filterList
    }

    override fun checkboxFilterPressOff(recipeCategory: String) {
        val filterList = filters.value
        filterList?.remove(recipeCategory)
        filters.value = filterList
    }

    override fun getStatusCheckBox(recipeCategory: String): Boolean {
        return filters.value?.contains(recipeCategory) == true
    }

    override fun removeStepClicked(step: Step) {
        repository.deleteStep(step)
    }

    override fun editStepClicked(step: Step) {
        currentStep.value = step
        navigateToStepEditScreenEvent.value = step
    }
}