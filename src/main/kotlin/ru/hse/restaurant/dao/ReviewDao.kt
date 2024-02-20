package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

interface ReviewDao {
    fun createReview(dish : DishEntity, login: String, text: String, stars: Int)
    fun deleteReview(review: ReviewEntity)
    fun editReview(review: ReviewEntity, newStars : Int, newText : String)
    fun getReviewsAboutDished(dish: DishEntity) : List<ReviewEntity>
    fun getAllReviews() : List<ReviewEntity>
}