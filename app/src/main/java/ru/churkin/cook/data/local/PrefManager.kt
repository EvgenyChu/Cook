package ru.churkin.cook.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.churkin.cook.App
import ru.churkin.cook.domain.Ingridient
import ru.churkin.cook.domain.Order
import ru.churkin.cook.recepts.Recept


object PrefManager {
    private val context = App.applicationContext()
    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun loadOrders(): List<Order> {
        val str = prefs.getString("MY_ORDERS", null)

        str ?: return emptyList()

        Log.e("PrefManager", "str $str")
        val orders = Json.decodeFromString(ListSerializer(Order.serializer()), str)
        Log.e("PrefManager", "obj $orders")
        return orders
    }

    fun insertOrder(order: Order) {

        val orders = loadOrders().plus(order)
        val str = Json.encodeToString(ListSerializer(Order.serializer()), orders)

        Log.e("PrefManager", "insert orders $str")

        prefs.edit()
            .putString("MY_ORDERS", str)
            .apply()
    }

    fun removeOrder(id: Int) {
        val curOrders = loadOrders()

        val index = curOrders.indexOfFirst { it.id == id }
        val orders = curOrders.toMutableList()
        orders.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Order.serializer()), orders)
        prefs.edit()
            .putString("MY_ORDERS", str)
            .apply()
    }

    fun insertRecept(recept: Recept) {
        val recepts = loadRecepts().plus(recept)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)

        Log.e("PrefManager", "insert orders $str")

        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()
    }

    fun loadRecepts(): List<Recept> {
        val str = prefs.getString("MY_RECEPTS", null)

        str ?: return emptyList()
        val recepts = Json.decodeFromString(ListSerializer(Recept.serializer()), str)
        return recepts
    }

    fun removeRecept(id: Int) {
        val curRecepts = loadRecepts()

        val index = curRecepts.indexOfFirst { it.id == id }
        val recepts = curRecepts.toMutableList()
        recepts.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)
        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()
    }

    fun insertIngridient(ingridient: Ingridient) {
        val ingridients = loadIngridients().plus(ingridient)
        val str = Json.encodeToString(ListSerializer(Ingridient.serializer()), ingridients)

        prefs.edit()
            .putString("MY_INGRIDIENTS", str)
            .apply()
    }

    fun loadIngridients(): List<Ingridient> {
        val str = prefs.getString("MY_INGRIDIENTS", null)

        str ?: return emptyList()
        val ingridients = Json.decodeFromString(ListSerializer(Ingridient.serializer()), str)
        return ingridients
    }

    fun removeIngridient(id: Int) {
        val curIngridients = loadIngridients()

        val index = curIngridients.indexOfFirst { it.id == id }
        val ingridients = curIngridients.toMutableList()
        ingridients.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Ingridient.serializer()), ingridients)
        prefs.edit()
            .putString("MY_INGRIDIENTS", str)
            .apply()
    }
}