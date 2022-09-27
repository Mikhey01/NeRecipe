package ru.netology.recipebook.adapter

interface FilterInteractionListener {

    fun checkboxFilterPressOn(recipeCategory: String)

    fun checkboxFilterPressOff(recipeCategory: String)

    fun getStatusCheckBox(recipeCategory: String): Boolean
}