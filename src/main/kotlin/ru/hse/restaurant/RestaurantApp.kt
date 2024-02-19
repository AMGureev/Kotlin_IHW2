package ru.hse.restaurant

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity

class RestaurantApp(
    private val dishDao: DishDao,
    private val menuDao: MenuDao,
    private val orderDao: OrderDao,
    private val reviewDao: ReviewDao,
    private val personDao: AccountDao,
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
            if (orderDao.getStatus(orderDao.returnOrderById(orderId)!!) != "wait paid"){
                println("error")
                return
            }
            if (orderDao.getCostOfOrder(orderDao.returnOrderById(orderId)!!) <= money){
                orderDao.setStatus(orderDao.returnOrderById(orderId)!!, "paid")
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
    fun addDishToMenu(titleDish : String) {
        if (dishDao.returnDishByTitle(titleDish) == null) {
            println("error")
            return
        }
        menuDao.addDishToMenu(dishDao.returnDishByTitle(titleDish)!!)
        println("congratulation!")
    }
    fun printAllDishesInMenu() {
        var cou = 1
        if (menuDao.returnAllDishes().isEmpty()) {
            println("menu is empty!")
            return
        }
        println("Menu's dishes:")
        for (dish in menuDao.returnAllDishes()){
            println("${cou++}. Dish : , price : ${dish.price}$")
        }
    }

    fun printAllReviews(dishTitle : String) {
        var cou = 1
        if (dishDao.returnDishByTitle(dishTitle) == null) {
            println("error")
            return
        }
        if (dishDao.returnAllReviewsAboutDish(dishDao.returnDishByTitle(dishTitle)!!).isEmpty()) {
            println("reviews list is empty!")
            return
        }
        println("All reviews about this dishes(${dishTitle})")
        for (review in dishDao.returnAllReviewsAboutDish(dishDao.returnDishByTitle(dishTitle)!!)){
            println("${cou++}. User : ${review.userName}\nStars : ${review.stars}\nText: ${review.text}")
        }
    }

}