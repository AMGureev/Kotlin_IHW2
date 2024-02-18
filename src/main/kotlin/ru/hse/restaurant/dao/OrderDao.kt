package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.OrderEntity

interface OrderDao {
    fun createOrder( order : OrderEntity ) // create new order
    fun cancelOrder( order : OrderEntity ) // cancel order
    fun getStatus( order : OrderEntity ) : String // return order's status
    fun payOrder (order : OrderEntity) // pay order
    fun getCostOfOrder(order : OrderEntity) : Int // return total cost of order
}