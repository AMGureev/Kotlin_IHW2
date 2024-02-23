package ru.hse.restaurant.controller

import ru.hse.restaurant.entity.OrderEntity

class KitchenApp {
    private var cookingOrders = mutableListOf<OrderEntity>()
    private var finishedOrders = mutableListOf<OrderEntity>()
    private var chefs = mutableListOf<ChefService>(ChefService(this))
    // delete
    fun processOrders() {
        chefs.forEach { chef->
            if (cookingOrders.isNotEmpty()) {
                if (chef.isChefFree()){
                    val orderToCook = cookingOrders.removeAt(0)
                    chef.cooking(orderToCook)
                }
            }
        }
    }
    fun addFinishedOrder(order: OrderEntity) {
        finishedOrders.add(order)
    }
    fun addCookingOrder(order: OrderEntity) {
        cookingOrders.add(order)
    }
    fun cancelOrder(order: OrderEntity) {
        for (chef in chefs) {
            if (chef.order?.id == order.id) {
                chef.cancelOrder()
                println("Order is cancel!")
                break
            }
        }
    }
}