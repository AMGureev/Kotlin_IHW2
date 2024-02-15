package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.MenuEntity

interface MenuDao {
    fun addDishToMenu(dish: DishEntity)
    fun deleteDishWithMenu(dish: DishEntity)
    fun returnAllDishes() : List<DishEntity> // return all dishes
}