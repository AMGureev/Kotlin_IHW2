package ru.hse.restaurant

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

class RestaurantApp(
    private val dishDao: DishDao,
    private val menuDao: MenuDao,
    private val orderDao: OrderDao,
    private val reviewDao: ReviewDao,
    private val personDao: PersonDao,
)
{
    fun leaveReview(personLogin : String, id : Int, dishTitle : String, text : String, stars : Int) {
        if (orderDao.returnOrderById(id)?.status == "paid") {
            if (dishDao.returnDishByTitle(dishTitle) in orderDao.returnOrderById(id)!!.dishes) {
                if (stars in 0..5){
                    if (text.length >= 20) {
                        reviewDao.createReview(dishDao.returnDishByTitle(dishTitle)!!, personLogin, text, stars)
                        println("congratulation!")
                    }
                    else {
                        println("error")
                    }
                }
                else{
                    println("error")
                }
            }
            else {
                println("error")
            }
        } else {
            println("error")
        }
    }
    fun createNewDish( title: String,
                       price: Int,
                       duration: Int,
                       weight: Double, ) {
        if (title.length < 5){
            println("error")
            return
        }
        if (price <= 0){
            println("error")
            return
        }
        if (duration <= 0){
            println("error")
            return
        }
        if (weight <= 0){
            println("error")
            return
        }
        if (dishDao.returnDishByTitle(title) != null) {
            dishDao.createDish(DishEntity(title, price, duration, weight, mutableListOf<ReviewEntity>()))
            println("congratulation!")
        } else{
            println("error")
        }
    }
    fun payForTheOrder(orderId : Int, money: Int) {
        if (orderDao.returnOrderById(orderId) != null){
            if (orderDao.getCostOfOrder(orderDao.returnOrderById(orderId)!!) <= money){
                println("congratulation!")
            }
            else {
                println("U didn't have enough money")
                return
            }
        }
        else {
            println("error")
            return
        }
    }
}