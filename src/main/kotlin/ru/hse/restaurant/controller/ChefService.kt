package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.ChefDao
import ru.hse.restaurant.entity.ChefEntity
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import kotlin.concurrent.thread

class ChefService(val kitchen: KitchenApp): ChefDao {
    private var isFree = true
    var countDishes = 0
    var order : OrderEntity? = null
    private var process : Thread? = null
    override fun cooking(order: OrderEntity): OrderEntity {
        this.order = order
        isFree = false
        countDishes = order.dishes.size
        process = Thread {
            cookOrder(order)
        }
        process?.start()
        return order
    }

    override fun isChefFree(): Boolean {
        return isFree
    }

    private fun cookOrder(order: OrderEntity) {
        var elem = 0
        try {
            while (elem < countDishes) {
                Thread.sleep(order.dishes[elem].duration.toLong())
                ++elem
            }
        }
        catch (e : InterruptedException) {
            isFree = true
            println("Order is cancel (print in thread)")
            return
        }
        order.status = "ready"
        kitchen.addFinishedOrder(order)
        isFree = true
        println("ORDER HAS BEEN COOOOOOOCK")
    }
    fun cancelOrder() {
        try {
            process?.interrupt()
            process = null
        } catch (_: Exception){

        }
    }

}