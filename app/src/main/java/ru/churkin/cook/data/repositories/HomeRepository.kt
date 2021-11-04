package ru.churkin.cook.data.repositories

import ru.churkin.cook.data.local.PrefManager
import ru.churkin.cook.domain.Order
import ru.churkin.cook.recepts.Recept

class HomeRepository() {
    private val prefs = PrefManager
    private val orders: MutableList<Order> = mutableListOf<Order>()

    init {
        orders.addAll(prefs.loadOrders())
    }


    fun loadRecepts() = prefs.loadRecepts()
    fun loadOrders(): List<Order> = orders
    fun insertOrder(order: Order) {



        if (orders.find { it.id == order.id } != null) return
        orders.add(order)
        prefs.insertOrder(order)
    }

    fun removeOrder(id: Int?) {
//        if (id==null) return
        id ?: return
        val index = orders.indexOfFirst { it.id == id }
        orders.removeAt(index)
        prefs.removeOrder(id)
    }

    fun isEmptyOrders() = orders.isEmpty()

    fun countOrders() = orders.size
}