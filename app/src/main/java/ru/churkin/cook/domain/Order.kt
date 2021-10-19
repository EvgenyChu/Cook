package ru.churkin.cook.domain

import ru.churkin.cook.home.Recept
import ru.churkin.cook.home.TimeUnits
import ru.churkin.cook.home.add
import java.util.*

data class Order(
    val id: Int,
    val dishes: List<String>,
    val deadline: Date,
    val profit: Int,
    val price: Int,
    val customer: String?
) {
    companion object Factory {
        private var lastId: Int = -1

        fun makeOrder(
            recepts: List<Recept>,
            deadlineOffset: Int,
            customer: String
        ): Order {
            lastId += 1

            return Order(
                id = lastId,
                dishes = recepts.map { it.dish },
                deadline = Date().add(deadlineOffset, TimeUnits.DAY),
                profit = recepts
                    .map { it.costPrice * 0.3f }
                    .fold(0) { acc: Int, cur: Float -> (acc + cur).toInt() },
                price = recepts
                    .map { it.costPrice * 1.3f }
                    .fold(0) { acc: Int, cur: Float -> (acc + cur).toInt() },
                customer = customer
            )
        }
    }
}