package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

interface DishDao {
    fun createDish(dish: DishEntity) // add new dish
    fun deleteDish(dish: DishEntity) // delete dish
    fun editDish(dish: DishEntity, newTitle: String, newPrice: Double, newCount: Int, newDuration: Int, newWeight: Double) // edit dish
    fun returnDishByTitle(title: String): DishEntity? // return dish by title
    fun leaveReviewToDish(dish: DishEntity, review: ReviewEntity)
    fun returnAllReviewsAboutDish(dish: DishEntity): List<ReviewEntity>
    fun returnAllDishes(): List<DishEntity>
    fun fillingDishesData()
    fun saveAllDishes()
}