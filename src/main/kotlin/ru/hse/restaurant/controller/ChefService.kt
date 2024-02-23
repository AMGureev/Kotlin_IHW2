package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.ChefDao
import ru.hse.restaurant.entity.ChefEntity
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity

class ChefService(val kitchen: KitchenApp): ChefDao {
    private var isFree = true
    override fun cooking(order: OrderEntity): OrderEntity {
        val threads = order.dishes.map { dish ->
            Thread {
                cookDish(dish)
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        order.status = "ready"
        kitchen.addFinishedOrder(order)
        return order
    }

    override fun isChefFree(): Boolean {
        return isFree
    }

    private fun cookDish(dish : DishEntity) {
        Thread.sleep(dish.duration.toLong())
    }
}