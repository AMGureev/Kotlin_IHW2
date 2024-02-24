package ru.hse.restaurant.entity

import java.time.LocalDateTime

data class OrderEntity(
    val id: Int,
    val person: String,
    var status: String,
    var dishes: List<DishEntity>,
    val timeStart: LocalDateTime
)