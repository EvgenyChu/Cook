package ru.churkin.cook.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.churkin.cook.recepts.Recept
import ru.churkin.cook.home.TimeUnits
import ru.churkin.cook.home.add
import java.util.*

@Serializable
data class Order(
    val id: Int,
    val dishes: List<String>,
    @Serializable(with = DateSerializer::class)
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


@Serializer(forClass = DateSerializer::class)
object DateSerializer : KSerializer<Date> {

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(obj.time.toString())
    }

    override fun deserialize(input: Decoder): Date {
        return Date(input.decodeString().toLong())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
}



