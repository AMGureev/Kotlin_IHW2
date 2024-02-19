package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

class InMemoryReviewDao : ReviewDao {
    private var reviews = mutableListOf<ReviewEntity>()

    override fun createReview(dish : DishEntity, login: String, text: String, stars: Int) {
        reviews.add(ReviewEntity(dish, login, text, stars))
    }

    override fun deleteReview(review: ReviewEntity) {
        reviews.remove(review)
    }

    override fun editReview(review: ReviewEntity, newStars: Int, newText: String) {
        review.stars = newStars
        review.text = newText
    }

    override fun getReviewsAboutDished(dish: DishEntity) : List<ReviewEntity> {
        return reviews.filter { review ->
            review.dish == dish
        }
    }
}