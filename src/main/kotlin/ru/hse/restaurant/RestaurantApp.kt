package ru.hse.restaurant

import ru.hse.restaurant.dao.*

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
                        reviewDao.createReview(dishDao.returnDishByTitle(dishTitle), personLogin, text, stars)
                        println("congratulation!")
                    }
                }
            }
        }
    }
}