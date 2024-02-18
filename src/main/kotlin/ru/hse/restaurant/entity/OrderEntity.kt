package ru.hse.restaurant.entity

data class OrderEntity (
    val person : UserEntity,
    var status : String,
    var dishes : List<DishEntity>
)