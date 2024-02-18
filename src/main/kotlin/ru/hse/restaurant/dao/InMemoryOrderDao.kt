package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.OrderEntity

class InMemoryOrderDao : OrderDao {
    private var orders = mutableListOf<OrderEntity>()
    override fun createOrder(order: OrderEntity) {
        orders.add(order)
    }

    override fun cancelOrder(order: OrderEntity) {
        orders.remove(order)
    }

    override fun getStatus(order: OrderEntity): String {
        return order.status
    }

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
}