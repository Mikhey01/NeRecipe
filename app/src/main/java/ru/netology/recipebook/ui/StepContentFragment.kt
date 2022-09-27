package ru.netology.recipebook.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.recipebook.databinding.AddOrEditStepFragmentBinding
import ru.netology.recipebook.util.focusAndShowKeyboard
import ru.netology.recipebook.viewmodel.RecipeViewModel

class StepContentFragment : Fragment() {

    private val args by navArgs<StepContentFragmentArgs>()

    private val viewModel: RecipeViewModel by activityViewModels()

    private val contract =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            viewModel.currentImageStep.value = uri.toString()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AddOrEditStepFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        binding.stepContentEdit.setText(args.initialStepText)
        binding.stepContentEdit.focusAndShowKeyboard()


        binding.saveStep.setOnClickListener {
            onOkButtonClicked(binding)
        }


        binding.addImageStep.setOnClickListener {
            contract.launch(arrayOf("image/*"))
        }
    }.root

    private fun onOkButtonClicked(binding: AddOrEditStepFragmentBinding) {
        val text = binding.stepContentEdit.text

        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(RESULT_KEY, text.toString())
            setFragmentResult(
                REQUEST_CURRENT_RECIPE_KEY,
                resultBundle
            )
        }
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_CURRENT_RECIPE_KEY = "requestForSingleRecipeFragmentKey"
        const val RESULT_KEY = "recipeNewContent"
    }
}