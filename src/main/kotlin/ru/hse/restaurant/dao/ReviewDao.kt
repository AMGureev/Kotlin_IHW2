package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.ReviewEntity

interface ReviewDao {
    fun createReview(review: ReviewEntity)
    fun deleteReview(review: ReviewEntity)
    fun editReview(review: ReviewEntity, newStars : Int, newText : String)

}