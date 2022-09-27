package ru.netology.recipebook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.recipebook.R
import ru.netology.recipebook.adapter.StepsAdapter
import ru.netology.recipebook.databinding.SingleRecipeViewBinding
import ru.netology.recipebook.dto.Recipe
import ru.netology.recipebook.viewmodel.RecipeViewModel

class SingleRecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels()

    private val args by navArgs<SingleRecipeFragmentArgs>()

    private lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return SingleRecipeViewBinding.inflate(
            layoutInflater, container, false
        ).also { binding ->
            with(binding) {

                val adapter = StepsAdapter(viewModel)
                stepsRecyclerView.adapter = adapter

                recipe = viewModel.data.value?.let { listRecipe ->
                    listRecipe.firstOrNull {
                        it.id == args.idCurrentRecipe
                    }
                } as Recipe
                render(recipe)
                adapter.submitList(recipe.content)
                adapter.differ.submitList(recipe.content)

                viewModel.data.observe(viewLifecycleOwner) { listRecipe ->
                    if (listRecipe.none { it.id == args.idCurrentRecipe }) {
                        return@observe
                    }
                    recipe = listRecipe.firstOrNull {
                        it.id == args.idCurrentRecipe
                    } as Recipe
                    render(recipe)
                    adapter.submitList(recipe.content)
                    adapter.differ.submitList(recipe.content)
                }

                viewModel.navigateToRecipeEditOrAddScreenEvent.observe(viewLifecycleOwner) { initialContentRecipe ->
                        val direction =SingleRecipeFragmentDirections.toNewRecipeFragment(
                            initialContentRecipe.id,
                                NewRecipeFragment.REQUEST_CURRENT_RECIPE_KEY
                            )
                        findNavController().navigate(direction)
                    }

                viewModel.navigateToStepEditScreenEvent.observe(viewLifecycleOwner) { currentRecipe ->
                    val direction =
                        SingleRecipeFragmentDirections.singleRecipeFragmentToStepContentFragment(
                            currentRecipe.stepText
                        )
                    findNavController().navigate(direction)
                }

                viewModel.navigateToStepAddScreenEvent.observe(
                    viewLifecycleOwner
                ) { stepText ->
                    val direction =
                        SingleRecipeFragmentDirections.singleRecipeFragmentToStepContentFragment(
                            stepText
                        )
                    findNavController().navigate(direction)
                }

                setFragmentResultListener(
                    requestKey = StepContentFragment.REQUEST_CURRENT_RECIPE_KEY
                ) { requestKey, bundle ->
                    if (requestKey != StepContentFragment.REQUEST_CURRENT_RECIPE_KEY) return@setFragmentResultListener
                    val newTextStep = bundle.getString(
                        StepContentFragment.RESULT_KEY
                    ) ?: return@setFragmentResultListener
                    viewModel.onSaveButtonStepClicked(newTextStep)
                }

                onFavorite.setOnClickListener { viewModel.onAddFavoriteClicked(recipe) }

                fabSteps.setOnClickListener {
                    viewModel.onAddStepClicked(recipe)
                }

                val popupMenu by lazy {
                    PopupMenu(context, binding.options).apply {
                        inflate(R.menu.options_recipe)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.remove -> {
                                    findNavController().navigateUp()
                                    viewModel.removeClickListener(recipe)
                                    //findNavController().popBackStack()
                                    true
                                }
                                R.id.edit -> {
                                    findNavController().navigateUp()
                                    viewModel.editClickListener(recipe)
                                    true
                                }
                                else -> false
                            }
                        }
                    }
                }

                options.setOnClickListener { popupMenu.show() }
            }

        }.root
    }

    private fun SingleRecipeViewBinding.render(recipe: Recipe) {
        authorName.text = recipe.author
        titleRecipe.text = recipe.title
        recipeCategory.text = recipe.recipeCategory
        onFavorite.isChecked = recipe.isFavorite
    }
}