package ru.netology.recipebook.adapter

import ru.netology.recipebook.dto.Step

interface StepInteractionListener {

    fun removeStepClicked(step: Step)

    fun editStepClicked(step: Step)
}