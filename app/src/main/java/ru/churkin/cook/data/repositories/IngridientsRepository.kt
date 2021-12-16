package ru.churkin.cook.data.repositories

import ru.churkin.cook.data.local.PrefManager
import ru.churkin.cook.domain.Ingridient

class IngridientsRepository {
    private val prefs = PrefManager

    private val ingridients: MutableList<Ingridient> = mutableListOf(
    )

    init {
        ingridients.clear()
        ingridients.addAll(prefs.loadIngridients())
    }


    fun loadIngridients(): List<Ingridient> = ingridients
    fun insertIngridient(indigrient: Ingridient) {
        val newInd = ingridients.size+1
        ingridients.add(indigrient.copy(id = newInd))
        prefs.insertIngridient(indigrient)
    }

    fun removeIngridient(id: Int?) {
//        if (id==null) return
        id ?: return
        val index = ingridients.indexOfFirst { it.id == id }
        ingridients.removeAt(index)
        prefs.removeIngridient(id)
    }

    fun isEmptyIngridients() = ingridients.isEmpty()

    fun countIngridients() = ingridients.size
}
