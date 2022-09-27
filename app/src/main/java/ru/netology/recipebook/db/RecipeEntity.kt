package ru.netology.recipebook.db

import androidx.room.*

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "category")
    val recipeCategory: String,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false,
//    @ColumnInfo(name = "onShare")
//    val onShare: Boolean = false,
    @ColumnInfo(name = "indexNumber")
    val indexNumber: Long

)