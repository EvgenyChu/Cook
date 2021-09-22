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
            recepts: List<Recept>
        ): Order {
            lastId += 1
            val number = (0..(recepts.size-1)).random()
            val needRecept = recepts.find { it.number == number }

                return Order(
                    id = lastId,
                    dish = (needRecept!!.dish),
                    deadline = Date(),
                    profit = (needRecept!!.costPrice * 0.3f).toInt(),
                    price = (needRecept!!.costPrice * 1.3f).toInt(),
                    customer = "Анна"
                )
        }
    }
}