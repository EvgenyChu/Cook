package ru.churkin.cook.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.churkin.cook.data.repositories.HomeRepository
import ru.churkin.cook.domain.Order
import java.util.*

class HomeViewModel() : ViewModel() {
    val repository: HomeRepository = HomeRepository()


    val screenState = MutableStateFlow(initialState())

    private val currentState: HomeScreenState
        get() = screenState.value


    private fun initialState(): HomeScreenState {
        val orders = repository.loadOrders()
        val ordersState = if (orders.isEmpty()) OrdersState.Empty
        else OrdersState.Value(orders = orders.sortedBy { it.deadline })

        return HomeScreenState(
            receptsName = repository.loadRecepts().map { it.dish },
            ordersState = ordersState
        )
    }

    fun removeOrder() {
        val id = currentState.orderIdForRemove
        repository.removeOrder(id)

        val orders = repository.loadOrders()
        val ordersState = if (orders.isEmpty()) OrdersState.Empty
        else OrdersState.Value(orders = orders.sortedBy { it.deadline })

        screenState.value = currentState.copy(
            ordersState = ordersState,
            orderIdForRemove = null,
            isConfirmDialog = false
        )

    }

    fun addOrder(selectedDishes: List<String>, customer: String, deadLineOffset: Float) {
        val needRecepts = repository.loadRecepts().filter { selectedDishes.contains(it.dish) }
        val order =  Order.makeOrder(
                needRecepts,
                deadlineOffset = deadLineOffset.toInt(),
                customer = customer
            )
        val newTitle = if (repository.countOrders() > 1) "Заказы" else "Заказ"

        viewModelScope.launch {
            screenState.value = currentState.copy(
                ordersState = OrdersState.ValueWithMessage(
                    repository.loadOrders().sortedBy { it.deadline }),
                isCreateDialog = !currentState.isCreateDialog
            )
            delay(3000)
            repository.insertOrder(order)
            screenState.value = currentState.copy(
                title = newTitle,
                ordersState = OrdersState.Value(
                    orders = repository.loadOrders().sortedBy { it.deadline })
            )
        }
    }

    fun showCreateDialog() {
        screenState.value = currentState.copy(isCreateDialog = true)
    }

    fun hideCreateDialog() {
        screenState.value = currentState.copy(isCreateDialog = false)
    }


    fun showConfirmDialog(orderIdForRemove: Int) {
        screenState.value =
            currentState.copy(isConfirmDialog = true, orderIdForRemove = orderIdForRemove)
    }

    fun hideConfirmDialog() {
        screenState.value = currentState.copy(isConfirmDialog = false, orderIdForRemove = null)
    }

}

class WorkDay(
    val day: WeekDay,
    val date: Date
)

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}


data class HomeScreenState(
    val title: String = "Заказов пока нет",
    val ordersState: OrdersState = OrdersState.Empty,
    val receptsName: List<String>,
    val isCreateDialog: Boolean = false,
    val isConfirmDialog: Boolean = false,
    val orderIdForRemove: Int? = null
)

sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
    data class ValueWithMessage(val orders: List<Order>, val message: String = "Any message") :
        OrdersState()

}

