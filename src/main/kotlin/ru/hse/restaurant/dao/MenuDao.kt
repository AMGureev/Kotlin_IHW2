package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.MenuEntity

interface MenuDao {
    fun addDishToMenu(dish: DishEntity) // add one dish to menu
    fun deleteDishWithMenu(dish: DishEntity) // delete one dish to menu
    fun returnAllDishes(): List<DishEntity> // return all dishes
    fun returnDishByTitle(title: String): DishEntity? // return dish by title from menu
    fun fillingMenuData() // download menu data json
    fun saveAllMenu() // save data to json
}