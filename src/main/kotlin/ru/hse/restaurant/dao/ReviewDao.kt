package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

interface ReviewDao {
    fun createReview(dish: DishEntity, login: String, text: String, stars: Int) // create new review
    fun deleteReview(review: ReviewEntity) // delete review
    fun editReview(review: ReviewEntity, newStars: Int, newText: String)
    fun getReviewsAboutDished(dish: DishEntity): List<ReviewEntity> // return reviews list about dishes
    fun getAllReviews(): List<ReviewEntity> // get all reviews
    fun saveAllReviews() // save data to json
    fun fillingReviewsData() // download reviews data json
    fun getReviewUserAboutDished(user : String, dish : String): ReviewEntity? // return user reviews list about dishes
}