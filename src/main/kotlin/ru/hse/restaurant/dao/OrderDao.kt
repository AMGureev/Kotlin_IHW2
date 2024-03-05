package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import ru.hse.restaurant.entity.UserEntity

interface OrderDao {
    fun createOrder(person: UserEntity, dishes: List<DishEntity>): OrderEntity// create new order
    fun cancelOrder(order: OrderEntity) // cancel order
    fun getStatus(order: OrderEntity): String // return order's status
    fun payOrder(order: OrderEntity) // pay order (take paid status)
    fun returnOrderById(id: Int): OrderEntity? // return order by id
    fun setStatus(order: OrderEntity, status: String) // set order status
    fun returnOrdersByStatus(status: String): List<OrderEntity> // return order by their status
    fun returnRevenue(): Double // return total revenue
    fun returnOrdersByUser(login: String): List<OrderEntity> // return total orders by user
    fun addDishToOrder(order: OrderEntity, dish: DishEntity) // add dish to cooking order
    fun fillingOrdersData() // download orders data json
    fun saveAllOrders() // save data to json
    fun returnAllOrders(): List<OrderEntity> // return all orders
}