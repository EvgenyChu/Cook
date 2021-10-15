package ru.churkin.cook.domain

import ru.churkin.cook.home.HomeScreenState
import ru.churkin.cook.home.Recept
import ru.churkin.cook.home.TimeUnits
import ru.churkin.cook.home.add
import java.util.*

data class Order(
    val id: Int,
    val dish: String,
    val deadline: Date,
    val profit: Int,
    val price: Int,
    val customer: String?
) {
    companion object Factory {
        private var lastId: Int = -1

        fun makeOrder(
            recept: Recept,
            deadlineOffset: Int,
            customer: String
        ): Order {
            lastId += 1

                return Order(
                    id = lastId,
                    dish = (recept.dish),
                    deadline = Date().add(deadlineOffset, TimeUnits.DAY),
                    profit = (recept.costPrice * 0.3f).toInt(),
                    price = (recept.costPrice * 1.3f).toInt(),
                    customer = customer
                )
        }
    }
}