package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.*
import kotlin.system.exitProcess

class ConsoleControllerAdmin(admin : AccountEntity,
                             private val console: ConsoleController,
                             private val dishDao : InMemoryDishDao,
                             private val menuDao : InMemoryMenuDao,
                             private val orderDao : InMemoryOrderDao,
                             private val reviewDao : InMemoryReviewDao,
                             private val accountDao : InMemoryAccountDao,
                             private val kitchenApp: KitchenApp) : Controller {
    override fun launch() {
        printMainTable()
    }
    private fun printMainTable() {
        println("Main admin table")
        println("Choose one of the actions:")
        println("1. Interaction with dishes")
        println("2. Get stats about dishes")
        println("3. Info about orders")
        println("4. Info about accounts")
        println("5. Sign out")
        println("6. Exit program")
        print("Enter your choose: ")
        var ans : Int = 0
        try {
            ans = readln().toInt()
        }
        catch (e: Exception){
            println("Error!")
            printMainTable()
        }
        when (ans) {
            1-> {
                println("Interaction with dishes!")
                println("1. Create new dish")
                println("2. Delete dish")
                println("3. Check all dishes")
                println("4. Add a dish to the menu")
                println("5. Edit info about dish")
                print("Choose one of the actions: ")
                try {
                    ans = readln().toInt()
                }
                catch (e: Exception){
                    println("Error!")
                    printMainTable()
                }
                when (ans) {
                    1->{
                        println("Create new dish!")
                        print("Input title: ")
                        val title = readln()
                        print("Input price: ")
                        try {
                            val price = readln().toInt()
                            print("Input duration: ")
                            val duration = readln().toInt()
                            print("Input weight: ")
                            val weight = readln().toDouble()
                            createNewDish(title, price, duration, weight)
                            printMainTable()
                        } catch (ex: Exception){
                            println("error to convert to int...")
                        }
                    }
                    2-> {
                        println("Delete dish!")
                        print("Input dish's title: ")
                        val title = readln()
                        deleteDish(title)
                        printMainTable()
                    }
                    3-> {
                        var cou = 1
                        print("Check all dishes or only in menu(y/n)?: ")
                        val res = readln()
                        when (res) {
                            "y"->{
                                if (dishDao.returnAllDishes().isEmpty()) {
                                    println("zero dishes...")
                                }
                                println("All dishes:")
                                for (dish in dishDao.returnAllDishes()) {
                                    println("${cou++}. Title: ${dish.title}, price: ${dish.price}$, weight: ${dish.weight}, duration: ${dish.duration}")
                                    // добавить пункт в меню или нет
                                }
                            }
                            "n"->{
                                if (menuDao.returnAllDishes().isEmpty()) {
                                    println("menu is empty!")
                                    return
                                }
                                println("Menu's dishes:")
                                for (dish in menuDao.returnAllDishes()){
                                    println("${cou++}. Title: ${dish.title}, price: ${dish.price}\$, weight: ${dish.weight}, duration: ${dish.duration}")
                                }
                            }
                            else -> {
                                println("error!")
                            }
                        }
                    }
                    4->{
                        println("Add a dish to the menu!")
                        print("Input dish's title: ")
                        val title = readln()
                        addDishToMenu(title)
                    }
                    5->{
                        println("Edit info about dish!")
                        print("Input dish's title: ")
                        val title = readln()
                        print("Input new dish's title(if you want to leave, input nothing: ")
                        var newTitle = readln()
                        println("Input new dish's price(if you want to leave, input nothing or zero: ")
                        var newPrice = readln()
                        if (newPrice.isEmpty()){
                            newPrice = "0"
                        }
                        println("Input new dish's weight(if you want to leave, input nothing or zero: ")
                        var newWeight = readln()
                        if (newWeight.isEmpty()){
                            newWeight = "0"
                        }
                        println("Input new dish's duration(if you want to leave, input nothing or zero: ")
                        var newDuration = readln()
                        if (newDuration.isEmpty()){
                            newDuration = "0"
                        }
                        editInfoAboutDish(title, newTitle, newPrice.toInt(), newWeight.toDouble(), newDuration.toInt())
                    }
                }
                printMainTable()
            }
            2-> {
                //Реализовать функционал, позволяющий администратору просматривать статистику по
                // заказам и отзывам
                // (например, самые популярные блюда, средняя оценка блюд, количество заказов за период).
                println("Get stats!")
                println("1. Stats about dish")
                println("2. Average dishes rating")
                println("3. Revenue")
                print("Input your choose: ")
                val ans = readln()
                when (ans) {
                    "1"-> {
                        print("Input dish's title: ")
                        val title = readln()
                        if (dishDao.returnDishByTitle(title) != null) {
                            println("1. Get average stars")
                            println("2. Get all reviews")
                            // еще добавить потом!!!
                            print("Input your choose: ")
                            val otv = readln()
                            when (otv) {
                                "1" -> {
                                    getAverageStars(dishDao.returnDishByTitle(title)!!)
                                }
                                "2" -> {
                                    getAllReview(dishDao.returnDishByTitle(title)!!)
                                }
                            }
                        } else {
                            println("error!")
                        }
                    }
                    "2"->{
                        getAverageStarsAllDishes()
                    }
                    "3" -> {
                        returnRevenue()
                    }
                    else-> {
                        println("Go to main table...")
                    }
                }

                printMainTable()
            }
            3-> { // set status...
                /*
                1. В процессе cooking
                2. Ожидает оплаты ready
                3. Отменено canceled
                4. Оплачено paid
                 */
                println("1. In process - 'cooking'")
                println("2. Wait to pay - 'ready'")
                println("3. Cancelled by the user - 'canceled'")
                println("4. Paid for by the user - 'paid'")
                print("Select one of the order's status: ")
                val res = readln()
                var coun = 1
                when (res) {
                    "1" -> {
                        val status = "cooking"
                        println("All cooking orders:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                        if (coun == 1) {
                            println("Zero cooking orders")
                        }
                    }
                    "2" -> {
                        val status = "ready"
                        println("All ready orders:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                        if (coun == 1) {
                            println("Zero ready orders")
                        }
                    }
                    "3" -> {
                        val status = "canceled"
                        println("All canceled orders:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                        if (coun == 1) {
                            println("Zero canceled orders")
                        }
                    }
                    "4" -> {
                        val status = "paid"
                        println("All paid orders:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                        if (coun == 1) {
                            println("Zero paid orders")
                        }
                    }
                    else -> {
                        println("Go to main table...")
                    }
                }
                printMainTable()
            }
            4-> {
                println("Info about accounts")
                var coun = 1
                for (acc in accountDao.returnAllAccounts()) {
                    println("${coun++}. Login: ${acc.login}, rule: ${if (acc is AdminEntity) {
                        "admin"
                    } else {
                        "user"
                    }}.${if (acc is AdminEntity) {""} else {"\n" +
                            "Count of orders: ${orderDao.returnOrdersByUser(acc as UserEntity).size}"}}")
                }

            }
            5-> {
                println("Sign out!")
                console.launch()
            }
            6->{
                if (orderDao.returnOrdersByStatus("cooking").isNotEmpty()) {
                    println("Attention! ${orderDao.returnOrdersByStatus("cooking").size} orders are cooking! If you exit, these orders aren't saving!")
                    print("Exit or not?(EXIT/other): ")
                    val otv = readln()
                    when (otv) {
                        "EXIT" -> {
                            this.console.saveAllInformationToJson()
                            println("Exit program! Goodbye!")
                            exitProcess(0)
                        }
                        else -> {
                            println("THX YOU")
                        }
                    }
                } else {
                    this.console.saveAllInformationToJson()
                    println("Exit program! Goodbye!")
                    exitProcess(0)
                }
            }
        }
        printMainTable()
    }
    private fun createNewDish( title: String,
                       price: Int,
                       duration: Int,
                       weight: Double) {
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
        if (dishDao.returnDishByTitle(title) == null) {
            dishDao.createDish(DishEntity(title, price, duration, weight, mutableListOf<ReviewEntity>()))
            println("Congratulation!")
            print("Add this dish to menu?(y/other): ")
            val res = readln()
            when (res) {
                "y"->{
                    menuDao.addDishToMenu(dishDao.returnDishByTitle(title)!!)
                    println("This dish on menu!")
                }
                else ->{
                    println("Okay! Create new dish (not add to menu)")
                }
            }
        } else{
            println("error")
        }
    }

    private fun deleteDish(title : String) {
        if (dishDao.returnDishByTitle(title) == null) {
            // проверить - есть ли это блюдо в активных заказах
            println("Error!")
            return
        } else {
            print("Are you sure?(y/other): ")
            val res = readln()
            when (res) {
                "y"-> {
                    dishDao.deleteDish(dishDao.returnDishByTitle(title)!!)
                    println("Congratulation!")
                }
                else->{
                    println("Cancel!")
                }
            }
        }
    }
    private fun addDishToMenu(titleDish : String) {
        if (dishDao.returnDishByTitle(titleDish) == null) {
            println("error")
            return
        }
        if (dishDao.returnDishByTitle(titleDish)!! in menuDao.returnAllDishes()){
            println("This dish already on menu!")
            return
        }
        menuDao.addDishToMenu(dishDao.returnDishByTitle(titleDish)!!)
        println("congratulation!")
    }
    private fun editInfoAboutDish(oldTitle: String, newTitle: String, newPrice: Int, newWeight: Double, newDuration: Int) {
        var newTitle = newTitle
        var newPrice = newPrice
        var newWeight = newWeight
        var newDuration = newDuration
        if (newTitle.isEmpty()){
            newTitle = oldTitle
        }
        if (newTitle.length < 5){
            println("error")
            return
        }
        if (newPrice < 0){
            println("error")
            return
        }
        if (newWeight < 0){
            println("error")
            return
        }
        if (newDuration < 0){
            println("error")
            return
        }
        if (dishDao.returnDishByTitle(oldTitle) == null) {
            println("error")
            return
        }
        if (newWeight == 0.0) {
            newWeight = dishDao.returnDishByTitle(oldTitle)!!.weight
        }
        if (newPrice == 0) {
            newPrice = dishDao.returnDishByTitle(oldTitle)!!.price
        }
        if (newDuration == 0){
            newDuration = dishDao.returnDishByTitle(oldTitle)!!.duration
        }
        dishDao.editDish(dishDao.returnDishByTitle(oldTitle)!!, newTitle, newPrice, newDuration, newWeight)
        println("Congratulation!")
    }
    private fun getAverageStars(dish: DishEntity) {
        if (reviewDao.getReviewsAboutDished(dish).isEmpty()) {
            println("List is empty!")
            return
        }
        var cou = 0
        for (review in reviewDao.getReviewsAboutDished(dish)){
            cou += review.stars
        }
        println("Average stars = ${cou/reviewDao.getReviewsAboutDished(dish).size}.")
    }
    private fun getAllReview(dish : DishEntity) {
        if (reviewDao.getReviewsAboutDished(dish).isEmpty()) {
            println("List is empty!")
            return
        }
        var cou = 1
        for (review in reviewDao.getReviewsAboutDished(dish)){
            println("${cou++}. Name: ${review.userName}, stars: ${review.stars} ,text: ${review.text}")
        }
    }
    private fun getAverageStarsAllDishes() {
        var averageStars = 0
        for (elem in reviewDao.getAllReviews()) {
            averageStars += elem.stars
        }
        println("Average stars all dishes = ${averageStars/reviewDao.getAllReviews().size}.")
    }
    private fun returnRevenue() {
        println("Total revenue - ${orderDao.returnRevenue()}$.")
    }
}