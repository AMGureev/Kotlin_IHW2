package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity

interface DishDao {
    fun createDish(dish: DishEntity) // add new dish
    fun deleteDish(dish: DishEntity) // delete dish
    fun editDish(dish: DishEntity, newTitle: String, newPrice: Int, newDuration: Int, newWeight: Double) // edit dish
    fun returnDishByTitle(title: String) : DishEntity // return dish by title
}