package ru.netology.recipebook.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.recipebook.R
import ru.netology.recipebook.databinding.StepRecipeBinding
import ru.netology.recipebook.dto.Step


internal class  StepsAdapter(
    private val interactionListener: StepInteractionListener
) : ListAdapter<Step, StepsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  StepRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepItem = currentList[position]
        holder.bind(stepItem)
    }

    inner class ViewHolder(
        private val binding: StepRecipeBinding,
        listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.optionsStep).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.removeStepClicked(step)
                            true
                        }
                        R.id.edit -> {
                            listener.editStepClicked(step)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.optionsStep.setOnClickListener { popupMenu.show() }
        }

        @SuppressLint("SetTextI18n")
        fun bind(step: Step) {
            this.step = step

            with(binding) {
                stepContent.text = step.stepText
                imageStep.isVisible = step.picture.isNotBlank()
                if (step.picture.isNotBlank()) imageStep.setImageURI(
                    step.picture.toUri()
                )
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {

        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem.stepText == newItem.stepText

    }

    val differ = AsyncListDiffer(this, DiffCallback)
}