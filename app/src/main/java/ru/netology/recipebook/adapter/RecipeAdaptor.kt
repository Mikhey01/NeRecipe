package ru.netology.recipebook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.recipebook.databinding.CardRecipeBinding
import ru.netology.recipebook.dto.Recipe

internal class RecipeAdaptor(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdaptor.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipeItem = currentList[position]
        holder.bind(recipeItem)
    }

    inner class ViewHolder(
        private val binding: CardRecipeBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        init {
            binding.addFavorites.setOnClickListener { listener.onAddFavoriteClicked(recipe) }
            binding.deleted.setOnClickListener { listener.removeClickListener(recipe) }
            binding.root.setOnClickListener { listener.onRecipeClicked(recipe) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe

            with(binding) {
                authorName.text = recipe.author
                titleRecipe.text = recipe.title
                recipeCategory.text = recipe.recipeCategory
                addFavorites.isChecked = recipe.isFavorite
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.content == newItem.content
    }

    fun moveItem(from: Int, to: Int) {
        val list = currentList.toMutableList()
        val fromLocation = list[from]
        list.removeAt(from)
        if (to < from) {
            list.add(to + 1, fromLocation)
        } else {
            list.add(to - 1, fromLocation)
        }
    }

    fun getIndexFrom(from: Int): Long {
        return currentList.toMutableList()[from].id
    }

    fun getIdFrom(from: Int): Long {
        return currentList.toMutableList()[from].id
    }

    fun getIndexTo(to: Int): Long {
        return currentList.toMutableList()[to].id
    }

    fun getIdTo(to: Int): Long {
        return currentList.toMutableList()[to].id
    }
}