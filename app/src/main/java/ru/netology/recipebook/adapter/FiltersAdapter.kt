package ru.netology.recipebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.recipebook.databinding.FilterRecipeBinding


internal class FiltersAdapter(
    private val interactionListener: FilterInteractionListener
) : ListAdapter<String, FiltersAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: FilterRecipeBinding,
        listener: FilterInteractionListener

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipeCategory: String


        init {
            binding.checkboxCategoryFilter.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listener.checkboxFilterPressOn(recipeCategory)
                } else listener.checkboxFilterPressOff(recipeCategory)
            }
        }

        fun bind(recipeCategory: String) {

            this.recipeCategory = recipeCategory
            with(binding) {

                textCategoryFilter.text = recipeCategory
                binding.checkboxCategoryFilter.isChecked =
                    interactionListener.getStatusCheckBox(recipeCategory)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}

