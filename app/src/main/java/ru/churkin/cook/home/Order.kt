package ru.churkin.cook.home

import java.util.*

data class Order(
    val id: Int,
    val dish: String,
    val deadline: Date,
    val profit: Int,
    val price: Int,
    val customer: String? = null
) {
    companion object Factory {
        private var lastId: Int = -1

        fun makeOrder(
            recept: Recept
        ): Order {
            lastId += 1

                return Order(
                    id = lastId,
                    dish = (recept.dish),
                    deadline = Date(),
                    profit = (recept.costPrice * 0.3f).toInt(),
                    price = (recept.costPrice * 1.3f).toInt(),
                    customer = "Анна"
                )
        }
    }
}