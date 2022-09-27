package ru.netology.recipebook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.recipebook.R
import ru.netology.recipebook.adapter.FiltersAdapter
import ru.netology.recipebook.databinding.FilterRecipeFragmentBinding
import ru.netology.recipebook.viewmodel.RecipeViewModel

class FilterFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FilterRecipeFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val categoriesList = resources.getStringArray(R.array.category_names).toMutableList()

        val adapterFilter = FiltersAdapter(viewModel)
        binding.filterRecycleView.adapter = adapterFilter

        adapterFilter.submitList(categoriesList)

        binding.selectFilter.setOnClickListener {

            findNavController().popBackStack()
        }
    }.root
}