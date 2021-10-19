package ru.churkin.cook.recepts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.churkin.cook.home.HomeScreenState
import ru.churkin.cook.home.OrdersState
import ru.churkin.cook.home.Recept

class ReceptsViewModel(): ViewModel() {

    private val recepts: List<Recept> = listOf(
        Recept(0, "Торт", listOf("мука", "яйца", "сахар"), 500),
        Recept(1, "Пирожное", listOf("мука", "яйца", "сахар", "крем"), 100),
        Recept(2, "Эклеры", listOf("мука", "яйца", "сахар", "лимон"), 200),
        Recept(3, "Капкейки", listOf("мука", "яйца", "сахар", "лимон"), 300),
        Recept(4, "Кекс", listOf("мука", "яйца", "сахар", "лимон"), 600),
        Recept(5, "Безе", listOf("мука", "яйца", "сахар", "лимон"), 100),
        Recept(6, "Мусовый торт", listOf("мука", "яйца", "сахар", "лимон"), 670)
    )


    val screenState = MutableStateFlow(initialState())

    private val currentState: ReceptsScreenState
        get() = screenState.value


    private fun initialState(): ReceptsScreenState {

        return ReceptsScreenState(
            recepts = emptyList(),
            title = "Рецептов пока нет"
        )
    }


}

data class ReceptsScreenState(
    val title: String = "Рецепты",
    val recepts: List<Recept>,
)
