package ru.hse.restaurant.entity

data class DishEntity(
    var title: String,
    var price: Int,
    var duration: Int,
    var weight: Double,
    var reviews: List<ReviewEntity>
)
