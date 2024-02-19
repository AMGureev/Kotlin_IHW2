package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.InMemoryDishDao
import ru.hse.restaurant.dao.InMemoryMenuDao
import ru.hse.restaurant.dao.InMemoryOrderDao
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.ReviewEntity
import kotlin.system.exitProcess

class ConsoleControllerAdmin : Controller {
    private val dishDao = InMemoryDishDao()
    private val menuDao = InMemoryMenuDao()
    private val orderDao = InMemoryOrderDao()
    override fun launch() {
        printMainTable()
    }
    private fun printMainTable() {
        println("Main admin table")
        println("Choose one of the actions:")
        println("1. Interaction with dishes")
        println("2. Get stats")
        println("3. Info about orders")
        println("4. Exit program")
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
                println("Choose one of the actions: ")
                println("1. Create new dish")
                println("2. Delete dish")
                println("3. Check all dishes")
                println("4. Add a dish to the menu")
                println("5. Edit info about dish")
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
                        val price = readln().toInt()
                        print("Input duration: ")
                        val duration = readln().toInt()
                        print("Input weight: ")
                        val weight = readln().toDouble()
                        createNewDish(title, price, duration, weight)
                        printMainTable()
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
                printMainTable()
            }
            3-> { // set status...
                /*
                1. В процессе
                2. Ожидает оплаты
                3. Отменено
                4. Оплачено
                 */

                print("Select one of the order's status: ")
                val res = readln()
                var coun = 1
                when (res) {
                    "1" -> {
                        println("All ... dishes:")
                        for (order in orderDao.returnOrdersByStatus(res)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                    }
                    "2" -> {
                        println("All ... dishes:")
                        for (order in orderDao.returnOrdersByStatus(res)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                    }
                    "3" -> {
                        println("All ... dishes:")
                        for (order in orderDao.returnOrdersByStatus(res)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                    }
                    "4" -> {
                        println("All ... dishes:")
                        for (order in orderDao.returnOrdersByStatus(res)) {
                            println("${coun++}. ID: ${order.id}, person: ${order.person.login}, dishes: ${order.dishes}")
                        }
                    }
                    else -> {
                        "error"
                    }
                }
                printMainTable()
            }
            4->{
                println("Exit program! Goodbye!")
                exitProcess(0)
            }
        }
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
        if (dishDao.returnDishByTitle("title") == null) {
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
}