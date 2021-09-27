package ru.churkin.cook.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class CookViewModel() : ViewModel() {
    val days: List<WorkDay> = emptyList()

    private val orders: MutableList<Order> = mutableListOf()

    val recepts: List<Recept> = listOf(
        Recept(0,"Торт", listOf("мука", "яйца", "сахар"), 3000),
        Recept(1,"Пирожное", listOf("мука", "яйца", "сахар", "крем"), 250),
        Recept(2,"Эклер", listOf("мука", "яйца", "сахар", "лимон"), 150)
    )

    val screenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState( recepts = recepts))


    val currentState : HomeScreenState
        get() = screenState.value

    fun addOrder() {
        val order = Order.makeOrder(recepts = recepts)

        val newTitle = if (orders.size > 1) "Заказы" else "Заказ"

        viewModelScope.launch {
            screenState.value = currentState.copy(ordersState = OrdersState.ValueWithMessage(orders.toList()))
            delay(3000)
            orders.add(order)
            screenState.value = currentState.copy(title = newTitle, ordersState = OrdersState.Value(orders = orders.toList()))
        }
    }
    fun tugleDialog(){
        screenState.value = currentState.copy(isOpenDialog = true)
    }
}

class WorkDay(
    val day: WeekDay,
    val date: Date
) {

}

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
    val recepts: List<Recept>,
    val isOpenDialog: Boolean = false
)


sealed class OrdersState{
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders : List<Order>) : OrdersState()
    data class ValueWithMessage(val orders : List<Order>, val message:String="Any message") : OrdersState()
}
