package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.OrderEntity

interface ChefDao {
    fun cooking(order: OrderEntity): OrderEntity // the cooking process
    fun isChefFree(): Boolean // checking the availability status of the chef
    fun cookOrder(order: OrderEntity) // cooking every dish on order
    fun cancelOrder() // cancel cooking process
}