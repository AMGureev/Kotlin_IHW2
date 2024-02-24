package ru.hse.restaurant.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import ru.hse.restaurant.entity.ReviewEntity
import java.io.File
import kotlin.io.path.Path

class InMemoryReviewDao : ReviewDao {
    private var reviews = mutableListOf<ReviewEntity>()
    private val directoryPath = "reviews"
    private val fileName = "reviews.json"

    override fun createReview(dish: DishEntity, login: String, text: String, stars: Int) {
        reviews.add(ReviewEntity(dish, login, text, stars))
    }

    override fun deleteReview(review: ReviewEntity) {
        reviews.remove(review)
    }

    override fun editReview(review: ReviewEntity, newStars: Int, newText: String) {
        review.stars = newStars
        review.text = newText
    }

    override fun getReviewsAboutDished(dish: DishEntity): List<ReviewEntity> {
        return reviews.filter { review ->
            review.dish == dish
        }
    }

    override fun getAllReviews(): List<ReviewEntity> {
        return reviews.toList()
    }

    override fun saveAllReviews() {
        File(directoryPath).mkdirs()
        val file = Path(directoryPath, fileName).toFile()
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, reviews)
    }

    override fun fillingReviewsData() {
        File(directoryPath).mkdirs()
        val file = File(directoryPath, fileName)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        reviews = mapper.readValue<MutableList<ReviewEntity>>(file.readText())
    }
}