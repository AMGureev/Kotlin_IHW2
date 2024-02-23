package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.ChefEntity
import ru.hse.restaurant.entity.OrderEntity

interface ChefDao {
    fun cooking(order: OrderEntity): OrderEntity
    fun isChefFree(): Boolean
}