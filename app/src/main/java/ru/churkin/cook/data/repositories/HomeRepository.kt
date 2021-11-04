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

    private val recepts: List<Recept> = listOf(
        Recept(0, "Торт", "мука, яйца, сахар", 500),
        Recept(1, "Пирожное", "мука, яйца, сахар, крем", 100),
        Recept(2, "Эклеры", "мука, яйца, сахар, лимон", 200),
        Recept(3, "Капкейки", "мука, яйца, сахар, лимон", 300),
        Recept(4, "Кекс", "мука, яйца, сахар, лимон", 600),
        Recept(5, "Безе", "мука, яйца, сахар, лимон", 100),
        Recept(6, "Мусовый торт", "мука, яйца, сахар, лимон", 670)
    )

    fun loadRecepts() = recepts
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