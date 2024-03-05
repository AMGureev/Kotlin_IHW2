package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import kotlin.io.path.Path

class InMemoryDishDao : DishDao {
    private var dishes = mutableListOf<DishEntity>()
    private val directoryPath = "dishes"
    private val fileName = "dishes.json"
    override fun createDish(dish: DishEntity) {
        dishes.add(dish)
    }

    override fun deleteDish(dish: DishEntity) {
        dishes.remove(dish)
    }

    override fun editDish(dish: DishEntity, newTitle: String, newPrice: Double, newCount: Int, newDuration: Int, newWeight: Double) {
        dish.title = newTitle
        dish.weight = newWeight
        dish.price = newPrice
        dish.count = newCount
        dish.duration = newDuration
    }

    override fun returnDishByTitle(title: String): DishEntity? {
        return dishes.find { it.title == title }
    }

    override fun leaveReviewToDish(dish: DishEntity, review: ReviewEntity) {
        dish.reviews.addLast(review)
    }

    override fun returnAllReviewsAboutDish(dish: DishEntity): List<ReviewEntity> {
        return dish.reviews
    }

    override fun returnAllDishes(): List<DishEntity> {
        return dishes.toList()
    }

    override fun saveAllDishes() {
        File(directoryPath).mkdirs()
        val file = Path(directoryPath, fileName).toFile()
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, dishes)
    }

    override fun fillingDishesData() {
        File(directoryPath).mkdirs()
        val file = File(directoryPath, fileName)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        dishes = mapper.readValue<MutableList<DishEntity>>(file.readText())
    }
}