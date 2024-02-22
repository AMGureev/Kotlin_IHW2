package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.AccountEntity
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.UserEntity
import kotlin.system.exitProcess

class ConsoleControllerUser(val user: AccountEntity) : Controller{
    private val dishDao = InMemoryDishDao()
    private val menuDao = InMemoryMenuDao()
    private val orderDao = InMemoryOrderDao()
    private val reviewDao = InMemoryReviewDao()
    override fun launch() {
        printMainTable()
    }
    fun printMainTable() {
        println("Main user table")
        println("Choose one of the actions:")
        println("1. View the contents of the menu")
        println("2. Make a new order")
        println("3. Interaction with my orders (and edit order)")
        println("4. Exit program")
        print("Enter your choose: ")
        val ans = readln()
        when (ans) {
            "1"-> {
                println("1. View the contents of the menu")
                printAllMenu()
            }
            "2"-> {
                println("2. Make an order")
                /* формат такой:
                если одно блюдо - то просто название блюда
                если блюд несколько - то писать блюдо x"count"
                закончить формировать заказ: end
                 */
                println("Please, input dish's title: ")
                var ans = readln()
                val list = mutableListOf<DishEntity>()
                val regex = Regex("""(.+?)\s*x(\d+)""")
                while (ans != "end") {
                    val matchResult = regex.matchEntire(ans)
                    if (matchResult != null) {
                        val (dish, quantity) = matchResult.destructured
                        if (menuDao.returnDishesByTitle(dish) == null) {
                            println("error")
                        } else {
                            for (i in 0..quantity) {

                            }
                            list.add(menuDao.returnDishesByTitle(ans)!!)
                        }
                    }
                    ans = readln()
                }
                if (list.isEmpty()) {
                    println("Zero dishes... ERROR")
                } else {
                    // создается заказ
                    orderDao.createOrder(user as UserEntity, list.toList())
                }
            }
            "3"-> {
                println("3. Interaction with my orders")
                println("Choose one of the actions:")
                println("1. Print all orders")
                println("2. Print cooking orders")
                println("3. Print orders awaiting payment")
                println("4. Print paid orders")
                print("Enter your choose: ")
                val otv = readln()
                var coun = 0
                when (otv) {
                    "1" -> {
                        println("All orders:")
                        if (orderDao.returnOrdersByUser(user as UserEntity).isEmpty()) {
                            println("Orders is empty!")
                        } else {
                            for (order in orderDao.returnOrdersByUser(user)) {
                                println("${coun++}. ID: ${order.id}, dishes: ${order.dishes}, status: ${order.status}.")
                            }
                        }
                    }
                    "2" -> {
                        val status = "cooking"
                        println("All cooking orders:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            if (order in orderDao.returnOrdersByUser(user as UserEntity)) {
                                println("${coun++}. ID: ${order.id}, dishes: ${order.dishes}, status: ${order.status}. ")
                            }
                        }
                        if (coun == 0) {
                            println("Orders is empty!")
                        }
                    }
                    "3" -> {
                        val status = "ready"
                        println("Orders awaiting payment:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            if (order in orderDao.returnOrdersByUser(user as UserEntity)) {
                                println("${coun++}. ID: ${order.id}, dishes: ${order.dishes}, status: ${order.status}. ")
                            }
                        }
                        if (coun == 0) {
                            println("Orders is empty!")
                        }
                    }
                    "4" -> {
                        val status = "paid"
                        println("Orders awaiting payment:")
                        for (order in orderDao.returnOrdersByStatus(status)) {
                            if (order in orderDao.returnOrdersByUser(user as UserEntity)) {
                                println("${coun++}. ID: ${order.id}, dishes: ${order.dishes}, status: ${order.status}. ")
                            }
                        }
                        if (coun == 0) {
                            println("Orders is empty!")
                        }
                    }
                    else -> {
                        println("Go to main table...")
                    }
                }
            }
            "4" ->{
                println("Exit program! Goodbye!")
                exitProcess(0)
            }
        }
    }
    private fun printAllMenu() {
        var cou = 1
        if (menuDao.returnAllDishes().isEmpty()) {
            println("menu is empty!")
            return
        }
        println("Menu's dishes:")
        for (dish in menuDao.returnAllDishes()){
            println("${cou++}. Title: ${dish.title}, price: ${dish.price}\$, weight: ${dish.weight}.")
        }
    }
    private fun returnOrdersByUser() {
        /*
            TODO
        */
    }
}