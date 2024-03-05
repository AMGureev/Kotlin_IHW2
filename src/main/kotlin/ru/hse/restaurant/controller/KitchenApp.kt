package ru.hse.restaurant.controller

import ru.hse.restaurant.entity.OrderEntity

class KitchenApp {
    private var cookingOrders = mutableListOf<OrderEntity>()
    private var finishedOrders = mutableListOf<OrderEntity>()
    private var chefs = mutableListOf<ChefService>(ChefService(this))

    // delete
    fun processOrders() { // process orders
        chefs.forEach { chef ->
            if (cookingOrders.isNotEmpty()) {
                if (chef.isChefFree()) {
                    val orderToCook = cookingOrders.removeAt(0)
                    chef.cooking(orderToCook)
                }
            }
        }
    }

    fun addFinishedOrder(order: OrderEntity) { // add new finished order to list
        finishedOrders.add(order)
    }

    fun addCookingOrder(order: OrderEntity) { // add new cooking order to list
        cookingOrders.add(order)
    }

    fun cancelOrder(order: OrderEntity) { // cancel cooking process
        for (chef in chefs) {
            if (chef.order?.id == order.id) {
                chef.cancelOrder()
                println("Order is cancel!")
                break
            }
        }
    }

    fun addDishToOrder(order: OrderEntity) { // add one dish to order
        for (chef in chefs) {
            if (chef.order?.id == order.id) {
                chef.countDishes += 1
                break
            }
        }
    }
}