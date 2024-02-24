package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.AccountEntity
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.UserEntity
import kotlin.system.exitProcess

class ConsoleControllerUser(private val user: AccountEntity,
                            private val console: ConsoleController,
                            private val dishDao : InMemoryDishDao,
                            private val menuDao : InMemoryMenuDao,
                            private val orderDao : InMemoryOrderDao,
                            private val reviewDao : InMemoryReviewDao,
                            private val kitchenApp: KitchenApp) : Controller{
    override fun launch() {
        printMainTable()
    }
    private fun printMainTable() {
        println("Main user table")
        println("Choose one of the actions:")
        println("1. View the contents of the menu")
        println("2. Make a new order")
        println("3. Interaction with my orders (and edit order)")
        println("4. Make a review")
        println("5. Sign out")
        println("6. Exit program")
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
                            repeat(quantity.toInt()) {
                                list.add(menuDao.returnDishesByTitle(dish)!!)
                            }
                        }
                    }
                    ans = readln()
                }
                if (list.isEmpty()) {
                    println("Zero dishes... ERROR")
                } else {
                    // создается заказ
                    println("Your order has been formed and submitted for processing!")
                    kitchenApp.addCookingOrder(orderDao.createOrder(user as UserEntity, list.toList()))
                    kitchenApp.processOrders()
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
                var coun = 1
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
                        if (coun == 1) {
                            println("Orders is empty!")
                        } else {
                            println("1. Edit order")
                            println("2. Cancel order")
                            print("Your opinion: ")
                            val ans = readln()
                            when (ans) {
                                "1" -> {
                                    print("Input ID order if you want to edit it (add new dish): ")
                                    try {
                                        val res = readln().toInt()
                                        if (orderDao.returnOrderById(res)!!.status == "cooking" && orderDao.returnOrderById(res)!! in  orderDao.returnOrdersByUser(user as UserEntity)) {
                                            print("Input title dish: ")
                                            val dish = readln()
                                            if (dishDao.returnDishByTitle(dish) != null) {
                                                if (dishDao.returnDishByTitle(dish)!! in menuDao.returnAllDishes()) {
                                                    orderDao.addDishToOrder(orderDao.returnOrderById(res)!!, dishDao.returnDishByTitle(dish)!!)
                                                    kitchenApp.addDishToOrder(orderDao.returnOrderById(res)!!)
                                                    println("Congratulation! You add $dish")
                                                } else {
                                                    println("Error - this dish is not on menu!")
                                                }
                                            } else {
                                                println("Error - this dish is not defined!")
                                            }
                                        }
                                    } catch (ex : Exception) {
                                        println("error convert to int...")
                                    }
                                }
                                "2" -> {
                                    print("Input ID order if you want to cancel it: ")
                                    try {
                                        val res = readln().toInt()
                                        if (orderDao.returnOrderById(res)!!.status == "cooking" && orderDao.returnOrderById(res)!! in  orderDao.returnOrdersByUser(user as UserEntity)) {
                                            orderDao.setStatus(orderDao.returnOrderById(res)!!, "canceled")
                                            kitchenApp.cancelOrder(orderDao.returnOrderById(res)!!)
                                        }
                                    } catch (ex : Exception) {
                                        println("error convert to int...")
                                    }
                                }
                                else -> {

                                }
                            }
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
                        if (coun == 1) {
                            println("Orders is empty!")
                        } else {
                            print("Input ID order and pay for it: ")
                            try {
                                val payId = readln().toInt()
                                if (orderDao.returnOrderById(payId)!!.status == "ready" && orderDao.returnOrderById(payId)!! in  orderDao.returnOrdersByUser(user as UserEntity)) {
                                    print("Are you sure?(y/other): ")
                                    val res = readln()
                                    when (res) {
                                        "y"-> {
                                            orderDao.setStatus(orderDao.returnOrderById(payId)!!, "paid")
                                            println("Congratulation!")
                                        }
                                        else->{
                                            println("Cancel!")
                                        }
                                    }
                                }
                            } catch (ex: Exception) {
                                println("error convert to int...")
                            }
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
                        if (coun == 1) {
                            println("Orders is empty!")
                        }
                    }
                    else -> {
                        println("Go to main table...")
                    }
                }
            }
            "4" -> {
                println("Make a review!")
                var coun = 1
                for (order in orderDao.returnOrdersByStatus("paid")) {
                    if (order in orderDao.returnOrdersByUser(user as UserEntity)) {
                        println("${coun++}. ID: ${order.id}, dishes: ${order.dishes}, status: ${order.status}. ")
                    }
                }
                if (coun == 1) {
                    println("You aren't having a paid order...!")
                } else {
                    print("Input ID: ")
                    try {
                        val otv = readln().toInt()
                        coun = 1
                        if (orderDao.returnOrderById(otv) != null && orderDao.returnOrderById(otv)!!.status == "paid") {
                            for (dish in orderDao.returnOrderById(otv)!!.dishes) {
                                println("${coun++}. Dish: ${dish.title}, price: ${dish.price}.")
                            }
                            print("Input title dish: ")
                            val dish = readln()
                            if (dishDao.returnDishByTitle(dish) in menuDao.returnAllDishes() && dishDao.returnDishByTitle(
                                    dish
                                ) in orderDao.returnOrderById(otv)!!.dishes
                            ) {
                                print("Input stars (0-5): ")
                                try {
                                    val stars = readln().toInt()
                                    print("Input text: ")
                                    val text = readln()
                                    if (stars in 0..5) {
                                        if (text.length >= 20) {
                                            reviewDao.createReview(
                                                dishDao.returnDishByTitle(dish)!!,
                                                user.login,
                                                text,
                                                stars
                                            )
                                            println("congratulation!")
                                        } else {
                                            println("error")
                                        }
                                    } else {
                                        println("error")
                                    }
                                } catch (ex: Exception) {
                                    println("Error!")
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        println("Error!")
                    }
                }
            }
            "5" -> {
                println("Sign out")
                console.launch()
            }
            "6" -> {
                println("Exit program! Goodbye!")
                exitProcess(0)
            }
        }
        printMainTable()
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