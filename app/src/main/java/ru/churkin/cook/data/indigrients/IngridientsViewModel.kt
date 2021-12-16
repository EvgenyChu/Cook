package ru.churkin.cook.data.indigrients

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.churkin.cook.data.repositories.IngridientsRepository
import ru.churkin.cook.domain.Ingridient
import java.util.*

class IngridientsViewModel() : ViewModel() {

    val repository: IngridientsRepository = IngridientsRepository()

    val screenState = MutableStateFlow(initialState())

    private val currentState: IngridientsScreenState
        get() = screenState.value

    fun hideCreateDialog() {
        screenState.value = currentState.copy(isCreateDialog = false)
    }

    fun showCreateDialog() {
        screenState.value = currentState.copy(isCreateDialog = true)
    }

    fun showRemoveDialog(ingridientIdForRemove: Int?) {
        screenState.value =
            currentState.copy(isConfirm = true, ingridientsIdForRemove = ingridientIdForRemove)
    }

    fun addIngridient(title: String, costPrice: Int, avaliable: Int, buyAt: Date) {
        val ingridient = Ingridient.makeIngridient(
            title = title,
            costPrice = costPrice,
            avaliable = avaliable,
            buyAt = buyAt
        )
        repository.insertIngridient(ingridient)
        val ingridients = repository.loadIngridients().sortedBy { it.title }
        val newTitle = if (ingridients.size > 1) "Ингридиенты" else "Ингридиент"
        screenState.value = currentState.copy(
            title = newTitle,
            ingridientsState = IngridientsState.Value(ingridients),
            isCreateDialog = false
        )
    }

    private fun initialState(): IngridientsScreenState {

        return IngridientsScreenState(
            ingridientsState = IngridientsState.Value(repository.loadIngridients()),
            ingridients = emptyList(),
            title = "Список ингридиентов"
        )
    }

    data class IngridientsScreenState(
        val title: String = "Ингридиенты",
        val ingridients: List<Ingridient>,
        val ingridientsState: IngridientsState = IngridientsState.Empty,
        val isCreateDialog: Boolean = false,
        val isConfirm: Boolean = false,
        val ingridientsIdForRemove: Int? = null
    )

}