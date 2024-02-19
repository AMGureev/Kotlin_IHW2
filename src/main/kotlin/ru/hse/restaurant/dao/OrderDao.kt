package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import ru.hse.restaurant.entity.UserEntity

interface OrderDao {
    fun createOrder(person : UserEntity, dishes : List<DishEntity>) // create new order
    fun cancelOrder( order : OrderEntity ) // cancel order
    fun getStatus( order : OrderEntity ) : String // return order's status
    fun payOrder (order : OrderEntity) // pay order
    fun getCostOfOrder(order : OrderEntity) : Int // return total cost of order
    fun returnOrderById(id : Int) : OrderEntity?
    fun setStatus(order: OrderEntity, status : String)
    fun returnOrdersByStatus(status: String) : List<OrderEntity>
}