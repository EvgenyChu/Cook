package ru.churkin.cook.data.repositories

import ru.churkin.cook.data.local.PrefManager
import ru.churkin.cook.domain.Order
import ru.churkin.cook.recepts.Recept

class ReceptsRepository {
    private val prefs = PrefManager
    private val recepts: MutableList<Recept> = mutableListOf(
        Recept(0, "Торт", "мука, яйца, сахар", 500),
        Recept(1, "Пирожное", "мука, яйца, сахар, крем", 100),
        Recept(2, "Эклеры", "мука, яйца, сахар, лимон", 200),
        Recept(3, "Капкейки", "мука, яйца, сахар, лимон", 300),
        Recept(4, "Кекс", "мука, яйца, сахар, лимон", 600),
        Recept(5, "Безе", "мука, яйца, сахар, лимон", 100),
        Recept(6, "Мусовый торт", "мука, яйца, сахар, лимон", 670)
    )

    init {
        recepts.clear()
        recepts.addAll(prefs.loadRecepts())
    }

    fun loadIngridients() = prefs.loadIngridients()
    fun loadRecepts(): List<Recept> = recepts
    fun insertRecept(recept: Recept) {
//        if (recepts.find { it.id == recept.id } != null) return
        val newInd = recepts.size+1
        recepts.add(recept.copy(id = newInd))
        prefs.insertRecept(recept)
    }

    fun removeRecept(id: Int?) {
//        if (id==null) return
        id ?: return
        val index = recepts.indexOfFirst { it.id == id }
        recepts.removeAt(index)
        prefs.removeRecept(id)
    }

    fun isEmptyRecepts() = recepts.isEmpty()

    fun countRecepts() = recepts.size
}