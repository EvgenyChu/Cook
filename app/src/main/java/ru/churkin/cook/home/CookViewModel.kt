package ru.churkin.cook.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.churkin.cook.domain.Order
import java.util.*

class CookViewModel() : ViewModel() {
    val days: List<WorkDay> = emptyList()


    private val orders: MutableList<Order> = mutableListOf()

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

    private val currentState: HomeScreenState
        get() = screenState.value


    private fun initialState(): HomeScreenState {

        return HomeScreenState(
            receptsName = recepts.map { it.dish }
        )
    }

    fun removeOrder() {
        val id = currentState.orderIdForRemove
    Log.e("CookViewModel", "$id for remove")
        val ind = orders.indexOfFirst { it.id == id }
        orders.removeAt(ind)
        screenState.value = currentState.copy(
            ordersState = OrdersState.Value(orders.sortedBy { it.deadline }),
            orderIdForRemove = null,
            isConfirm = false)
            if (orders.size==0){
                screenState.value = currentState.copy(
                    ordersState = OrdersState.Empty,
                    orderIdForRemove = null,
                    isConfirm = false
                )}
    }

    fun addOrder(isSelected: Int, customer: String, deadLineOffset: Float) {
        val recept = recepts.find { it.isSelected == isSelected }
        checkNotNull(recept) { "recept must be not null" }
        val order = Order.makeOrder(recept, deadlineOffset = deadLineOffset.toInt(), customer = customer)
        val newTitle = if (orders.size > 1) "Заказы" else "Заказ"

        viewModelScope.launch {
            screenState.value = currentState.copy(
                ordersState = OrdersState.ValueWithMessage(orders.sortedBy { it.deadline }),
                isOpenDialog = !currentState.isOpenDialog
            )
            delay(3000)
            orders.add(order)
            screenState.value = currentState.copy(
                title = newTitle,
                ordersState = OrdersState.Value(orders = orders.sortedBy { it.deadline })
            )
        }
    }

    fun toggleDialog() {
        screenState.value = currentState.copy(isOpenDialog = !currentState.isOpenDialog)
    }

    fun warningDialog() {
        screenState.value = currentState.copy(isConfirm = !currentState.isConfirm)
    }
    fun showRemoveDialog(orderIdForRemove: Int?){
        screenState.value = currentState.copy(isConfirm = true, orderIdForRemove = orderIdForRemove)
    }
    fun showEmptyDialog(orderIdForRemove: Int?){
        screenState.value = currentState.copy(isConfirm = false, ordersState = OrdersState.Empty)
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
    val isSelected: Int,
    val dish: String,
    val indigrients: List<String> = emptyList(),
    val costPrice: Int
)

data class HomeScreenState(
    val title: String = "Заказов пока нет",
    val ordersState: OrdersState = OrdersState.Empty,
    val receptsName: List<String>,
    val isOpenDialog: Boolean = false,
    val isConfirm: Boolean = false,
    val orderIdForRemove: Int? = null
)


sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
    data class ValueWithMessage(val orders: List<Order>, val message: String = "Any message") :
        OrdersState()

}
