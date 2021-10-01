package ru.churkin.cook.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class CookViewModel() : ViewModel() {
    val days: List<WorkDay> = emptyList()

    private val orders: MutableList<Order> = mutableListOf()

    private val recepts: List<Recept> = listOf(
        Recept(0, "Торт", listOf("мука", "яйца", "сахар"), 3000),
        Recept(1, "Пирожное", listOf("мука", "яйца", "сахар", "крем"), 250),
        Recept(2, "Эклер", listOf("мука", "яйца", "сахар", "лимон"), 150)
    )


    val screenState = MutableStateFlow(initialState())

    private val currentState: HomeScreenState
        get() = screenState.value


    private fun initialState(): HomeScreenState {

        return HomeScreenState(
            receptsName = recepts.map { it.dish }
        )
    }

    fun removeOrder(id: Int) {
    Log.e("CookViewModel", "$id for remove")
        val ind = orders.indexOfFirst { it.id == id }
        orders.removeAt(ind)
        screenState.value = currentState.copy(
            ordersState = OrdersState.Value(orders.toList()),
        )
    }

    fun addOrder(dish: String) {
        Log.e("CookViewModel", "$dish")
        val recept = recepts.find { it.dish == dish }
        checkNotNull(recept) { "recept must be not null" }
        val order = Order.makeOrder(recept)

        val newTitle = if (orders.size > 1) "Заказы" else "Заказ"

        viewModelScope.launch {
            screenState.value = currentState.copy(
                ordersState = OrdersState.ValueWithMessage(orders.toList()),
                isOpenDialog = !currentState.isOpenDialog
            )
            delay(3000)
            orders.add(order)
            screenState.value = currentState.copy(
                title = newTitle,
                ordersState = OrdersState.Value(orders = orders.toList())
            )
        }
    }

    fun tugleDialog() {
        screenState.value = currentState.copy(isOpenDialog = !currentState.isOpenDialog)
    }
}

class WorkDay(
    val day: WeekDay,
    val date: Date
)

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}


data class Recept(
    val number: Int,
    val dish: String,
    val indigrients: List<String> = emptyList(),
    val costPrice: Int
)

data class HomeScreenState(
    val title: String = "Заказов пока нет",
    val ordersState: OrdersState = OrdersState.Empty,
    val receptsName: List<String>,
    val isOpenDialog: Boolean = false
)


sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
    data class ValueWithMessage(val orders: List<Order>, val message: String = "Any message") :
        OrdersState()
}
