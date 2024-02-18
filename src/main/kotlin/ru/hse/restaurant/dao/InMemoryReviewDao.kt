package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.ReviewEntity

class InMemoryReviewDao : ReviewDao {
    private var feedbacks = mutableListOf<ReviewEntity>()
    override fun createReview(review: ReviewEntity) {
        feedbacks.add(review)
    }

    override fun deleteReview(review: ReviewEntity) {
        feedbacks.remove(review)
    }

    override fun editReview(review: ReviewEntity, newStars: Int, newText: String) {
        review.stars = newStars
        review.text = newText
    }
}