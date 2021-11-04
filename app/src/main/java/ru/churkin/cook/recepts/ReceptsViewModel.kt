package ru.churkin.cook.recepts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.churkin.cook.data.repositories.ReceptsRepository
import ru.churkin.cook.domain.Dish
import ru.churkin.cook.domain.Order
import ru.churkin.cook.home.OrdersState

class ReceptsViewModel(): ViewModel() {

    val repository: ReceptsRepository = ReceptsRepository()

    private val recepts: List<Recept> = listOf(
        Recept(0, "Торт", "мука, яйца, сахар", 500),
        Recept(1, "Пирожное", "мука, яйца, сахар, крем", 100),
        Recept(2, "Эклеры", "мука, яйца, сахар, лимон", 200),
        Recept(3, "Капкейки", "мука, яйца, сахар, лимон", 300),
        Recept(4, "Кекс", "мука, яйца, сахар, лимон", 600),
        Recept(5, "Безе", "мука, яйца, сахар, лимон", 100),
        Recept(6, "Мусовый торт", "мука, яйца, сахар, лимон", 670)
    )


    val screenState = MutableStateFlow(initialState())

    private val currentState: ReceptsScreenState
        get() = screenState.value


    private fun initialState(): ReceptsScreenState {

        return ReceptsScreenState(
            receptsState = ReceptsState.Value(repository.loadRecepts()),
            recepts = emptyList(),
            title = "Список рецептов"
        )
    }

    fun warningDialog() {
        screenState.value = currentState.copy(isConfirm = !currentState.isConfirm)
    }

    fun toggleReceptDialog() {
        screenState.value = currentState.copy(isOpenDialog = !currentState.isOpenDialog)
    }

    fun showCreateDialog() {
        screenState.value = currentState.copy(isOpenDialog = true)
    }

    fun showRemoveDialog(receptIdForRemove: Int?){
        screenState.value = currentState.copy(isConfirm = true, receptIdForRemove = receptIdForRemove)
    }

    fun addRecept(addDish: String, addIndigrients: String) {
        val recept = Recept.makeRecept(addDish = addDish, addIndigrients = addIndigrients)
        val newTitle = if (repository.countRecepts() > 1) "Рецепты" else "Рецепт"

        viewModelScope.launch {
            screenState.value = currentState.copy(
                receptsState = ReceptsState.ValueWithMessage(repository.loadRecepts().sortedBy { it.id }),
                isOpenDialog = !currentState.isOpenDialog
            )
            delay(3000)
            repository.insertRecept(recept)
            screenState.value = currentState.copy(
                title = newTitle,
                receptsState = ReceptsState.Value(recepts = repository.loadRecepts().sortedBy { it.id })
            )
        }
    }

    fun removeRecept() {
        val id = currentState.receptIdForRemove
        repository.removeRecept(id)
        screenState.value = currentState.copy(
            receptsState = ReceptsState.Value(repository.loadRecepts().sortedBy { it.id }),
            receptIdForRemove = null,
            isConfirm = false)
        if (repository.isEmptyRecepts()){
            screenState.value = currentState.copy(
                receptsState = ReceptsState.Empty,
                receptIdForRemove = null,
                isConfirm = false
            )}
    }

}

data class ReceptsScreenState(
    val title: String = "Рецепты",
    val recepts: List<Recept>,
    val receptsState: ReceptsState = ReceptsState.Empty,
    val isOpenDialog: Boolean = false,
    val isConfirm: Boolean = false,
    val receptIdForRemove: Int? = null
)

sealed class ReceptsState {
    object Loading : ReceptsState()
    object Empty : ReceptsState()
    data class Value(val recepts: List<Recept>) : ReceptsState()
    data class ValueWithMessage(val recepts: List<Recept>, val message: String = "Any message") :
        ReceptsState()

}

@Serializable
data class Recept(
    val id: Int,
    val dish: String,
    val indigrients: String,
    val costPrice: Int
) {
    companion object Factory {
        private var lastId: Int = 6

        fun makeRecept(addDish: String, addIndigrients: String): Recept {
            lastId += 1

            return Recept(
                id = lastId,
                dish = addDish,
                indigrients = addIndigrients,
                costPrice = 0
            )
        }
    }
}