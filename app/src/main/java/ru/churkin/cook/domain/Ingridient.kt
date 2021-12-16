package ru.churkin.cook.domain

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Ingridient(
    val id: Int,
    val title: String,
    val costPrice: Int,
    val avaliable: Int = 0,
    @Serializable(with = DateSerializer::class)
    val buyAt: Date = Date()
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeIngridient(title: String, costPrice: Int, avaliable: Int, buyAt: Date): Ingridient {
            lastId += 1

            return Ingridient(
                id = lastId,
                title = title,
                costPrice = costPrice,
                avaliable = avaliable,
                buyAt = Date()
            )
        }
    }
}
