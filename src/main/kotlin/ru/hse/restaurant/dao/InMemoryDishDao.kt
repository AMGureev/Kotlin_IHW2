package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

class InMemoryDishDao : DishDao {
    private var dishes = mutableListOf<DishEntity>()
    override fun createDish(dish: DishEntity) {
        dishes.add(dish)
    }

    override fun deleteDish(dish: DishEntity) {
        dishes.remove(dish)
    }

    override fun editDish(dish: DishEntity, newTitle: String, newPrice: Int, newDuration: Int, newWeight: Double) {
        dish.title = newTitle
        dish.weight = newWeight
        dish.price = newPrice
        dish.duration = newDuration
    }

    override fun returnDishByTitle(title: String): DishEntity? {
        return dishes.find { it.title == title }
    }

    override fun leaveReviewToDish(dish: DishEntity, review: ReviewEntity) {
        dish.reviews.addLast(review)
    }

    override fun returnAllReviewsAboutDish(dish: DishEntity) : List<ReviewEntity> {
        return dish.reviews
    }

}