package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.MenuEntity

class InMemoryMenuDao : MenuDao{
    private var dishes = mutableListOf<DishEntity>()
    override fun addDishToMenu(dish: DishEntity) {
        dishes.add(dish)
    }

    override fun deleteDishWithMenu(dish: DishEntity) {
        dishes.remove(dish)
    }

    override fun returnAllDishes(): List<DishEntity> {
        return dishes.toList()
    }

    override fun returnDishesByTitle(title : String) : DishEntity? {
        return dishes.find { it.title == title }
    }
}