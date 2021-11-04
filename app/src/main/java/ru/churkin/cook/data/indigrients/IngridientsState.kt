package ru.churkin.cook.data.indigrients

import ru.churkin.cook.domain.Ingridient

sealed class IngridientsState {
    object Loading : IngridientsState()
    object Empty : IngridientsState()
    data class Value(val ingridients: List<Ingridient>) : IngridientsState()
    data class ValueWithMessage(val indigrients: List<Ingridient>, val message: String = "Any message") :
        IngridientsState()

}