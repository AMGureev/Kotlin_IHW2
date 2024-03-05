package ru.hse.restaurant.entity

data class ReviewEntity(
    val dish: String,
    var userName: String,
    var text: String = "Text is empty",
    var stars: Int
)
