package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import ru.hse.restaurant.entity.UserEntity
import java.time.LocalDateTime
import java.time.LocalTime

class InMemoryOrderDao : OrderDao {
    private var orders = mutableListOf<OrderEntity>()
    private var lastId = 0
    override fun createOrder(person : UserEntity, dishes : List<DishEntity>) : OrderEntity {
        val time = LocalDateTime.now()
        val order = OrderEntity(lastId, person, "create", dishes, time)
        orders.add(order)
        lastId += 1
        return order
    }

    override fun cancelOrder(order: OrderEntity) {
        orders.remove(order)
    }
    /*
    fun processOrder(order: OrderEntity) {
        val threads = order.dishes.map{ dish->
            Thread {
                cookDish(dish)
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        order.status = "ready"
    }
     */
    override fun getStatus(order: OrderEntity): String {
        return order.status
    }
    /*
    private fun cookDish(dish : DishEntity) {
        Thread.sleep(dish.duration.toLong())
    }
     */

    override fun payOrder(order: OrderEntity) {
        order.status = "paid"
    }

    override fun getCostOfOrder(order: OrderEntity): Int {
        var sum = 0
        for (i in 0..order.dishes.size) {
            sum += order.dishes[i].price
        }
        return sum
    }

    override fun returnOrderById(id: Int): OrderEntity? {
        return orders.find { it.id == id }
    }

    override fun setStatus(order: OrderEntity, status : String) {
        order.status = status
    }

    override fun returnOrdersByStatus(status: String): List<OrderEntity> {
        return orders.filter { order ->
            order.status == status
        }
    }

    override fun returnRevenue(): Int {
        var revenue = 0
        for (elem in returnOrdersByStatus("paid")) {
            for (dish in elem.dishes) {
                revenue += dish.price
            }
        }
        return revenue
    }

    override fun returnOrdersByUser(user: UserEntity): List<OrderEntity> {
        return orders.filter { order ->
            order.person == user
        }
    }

}