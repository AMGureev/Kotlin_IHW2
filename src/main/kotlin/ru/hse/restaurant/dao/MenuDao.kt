package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.MenuEntity

interface MenuDao {
    fun addDishToMenu(dish: DishEntity) // add one dish to menu
    fun deleteDishWithMenu(dish: DishEntity) // delete one dish to menu
    fun returnAllDishes(): List<DishEntity> // return all dishes
    fun returnDishesByTitle(title: String): DishEntity?
    fun fillingMenuData()
    fun saveAllMenu()
}