package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.AccountEntity
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.UserEntity

class ConsoleControllerUser(
    private val user: AccountEntity,
    private val console: ConsoleController,
    private val dishDao: InMemoryDishDao,
    private val menuDao: InMemoryMenuDao,
    private val orderDao: InMemoryOrderDao,
    private val reviewDao: InMemoryReviewDao,
    private val kitchenApp: KitchenApp
) : Controller {
    override fun launch() {
        printMainTable()
    }

    private fun printMainTable() { // a dialog table with the user-client
        println("Main user table")
        println("Choose one of the actions:")
        println("1. View the contents of the menu")
        println("2. Make a new order")
        println("3. Interaction with my orders (and edit order)")
        println("4. Reviews")
        println("5. Sign out")
        println("6. Exit program")
        print("Choose one of the actions: ")
        val ans = readln()
        when (ans) {
            "1" -> {
                println("View the contents of the menu")
                printAllMenu()
            }

            "2" -> {
                makeAnOrder()
            }

            "3" -> {
                interactionWithOrders()
            }

            "4" -> {
                interactionWithReview()
            }

            "5" -> {
                println("Sign out")
                console.launch()
            }

            "6" -> {
                this.console.exitProgram()
            }
        }
        printMainTable()
    }

    private fun printAllMenu() { // print all dishes from the menu
        var cou = 1
        if (menuDao.returnAllDishes().isEmpty()) {
            println("The menu is empty!")
            return
        }
        println("The menu's dishes:")
        for (dish in menuDao.returnAllDishes()) {
            println("${cou++}. Title: ${dish.title}, price: ${dish.price}, weight: ${dish.weight}.")
        }
    }

    private fun makeAnOrder() { // create a new order
        println("Make an order")
        /* формат такой:
        блюд несколько - то писать блюдо x"count", где count - кол-во блюд
        закончить формировать заказ: end
        */
        println("Please, input dish's title: ")
        println("Input format: {dish name} x{quantity}")
        print(">>")
        var ans = readln()
        val list = mutableListOf<DishEntity>()
        val regex = Regex("""(.+?)\s*x(\d+)""")
        while (ans != "end") {
            val matchResult = regex.matchEntire(ans)
            if (matchResult != null) {
                val (dish, quantity) = matchResult.destructured
                if (quantity.isEmpty()) {
                    println("ERROR [Incorrect format]")
                }
                if (menuDao.returnDishByTitle(dish) == null) {
                    println("ERROR [This dish is not on the menu]")
                } else {
                    if (quantity.toInt() <= menuDao.returnDishByTitle(dish)!!.count && quantity.toInt() > 0) {
                        repeat(quantity.toInt()) {
                            list.add(menuDao.returnDishByTitle(dish)!!)
                        }
                        removeCountDishes(dish, quantity.toInt())
                    } else {
                        println("ERROR [Incorrect value of the number of dishes]")
                    }
                }
            }
            print(">>")
            ans = readln()
        }
        if (list.isEmpty()) {
            println("The order was not formed because you did not order anything!")
        } else {
            // создается заказ
            println("Your order has been formed and submitted for processing! Wait!")
            kitchenApp.addCookingOrder(orderDao.createOrder(user as UserEntity, list.toList()))
            kitchenApp.processOrders()
        }
    }

    private fun interactionWithOrders() { // user interaction with orders
        println("Interaction with my orders (and edit order)")
        println("Choose one of the actions:")
        println("1. Print all orders")
        println("2. Print cooking orders")
        println("3. Print orders awaiting payment(paid orders)")
        println("4. Print paid orders")
        print("Choose one of the actions: ")
        val otv = readln()
        var coun = 1
        when (otv) {
            "1" -> {
                println("All orders:")
                if (orderDao.returnOrdersByUser(user.login).isEmpty()) {
                    println("Orders is empty!")
                } else {

                    for (order in orderDao.returnOrdersByUser(user.login)) {
                        val dishCountMap = mutableMapOf<String, Int>()
                        for (dish in order.dishes) {
                            val title = dish.title
                            dishCountMap[title] = dishCountMap.getOrDefault(title, 0) + 1
                        }
                        print("${coun++}. ID: ${order.id}, status: ${order.status}, dishes: ")
                        for ((dish, count) in dishCountMap) {
                            print("$dish x$count, ")
                        }
                        println()
                    }
                }
            }

            "2" -> {
                println("All cooking orders:")
                printOrders("cooking")
                if (orderDao.returnOrdersByUser(user.login).filter { order -> order.status == "cooking" }
                        .isEmpty()) {

                } else {
                    println("1. Edit order")
                    println("2. Cancel order")
                    println("3. Go to main table")
                    print("Choose one of the actions: ")
                    val ans = readln()
                    when (ans) {
                        "1" -> {
                            editOrder()
                        }

                        "2" -> {
                            cancelOrder()
                        }

                        "3" -> {

                        }
                    }
                }
            }

            "3" -> {
                println("Orders awaiting payment:")
                printOrders("ready")
                if (orderDao.returnOrdersByUser(user.login).filter { order -> order.status == "ready" }
                        .isEmpty()) {

                } else {
                    println("1. Pay for the order")
                    println("2. Go back to the user table")
                    print("Choose one of the actions: ")
                    val opinion = readln()
                    when (opinion) {
                        "1" -> {
                            print("Input ID order and pay for it: ")
                            try {
                                val payId = readln().toInt()
                                if (orderDao.returnOrderById(payId)!!.status == "ready" && orderDao.returnOrderById(
                                        payId
                                    )!! in orderDao.returnOrdersByUser(
                                        user.login
                                    )
                                ) {
                                    print("Are you sure?(y/other): ")
                                    val res = readln()
                                    when (res) {
                                        "y" -> {
                                            orderDao.payOrder(orderDao.returnOrderById(payId)!!)
                                            println("Congratulation!")
                                        }

                                        else -> {
                                            println("Cancel!")
                                        }
                                    }
                                } else {
                                    println("ERROR [Invalid value ID]")
                                }
                            } catch (ex: Exception) {
                                println("ERROR [It was required to enter a number]")
                            }
                        }

                        "2" -> {

                        }
                    }
                }
            }

            "4" -> {
                println("All paid orders: ")
                printOrders("paid")
            }

            else -> {
                println("Go to main table...")
            }
        }
    }

    private fun editOrder() { // edit order
        print("Input ID order if you want to edit it (add new dish): ")
        try {
            val res = readln().toInt()
            if (orderDao.returnOrderById(res)!!.status == "cooking" && orderDao.returnOrderById(res)!! in orderDao.returnOrdersByUser(
                    user.login
                )
            ) {
                print("Input title dish: ")
                val dish = readln()
                if (dishDao.returnDishByTitle(dish) != null) {
                    if (dishDao.returnDishByTitle(dish)!! in menuDao.returnAllDishes()) {
                        orderDao.addDishToOrder(orderDao.returnOrderById(res)!!, dishDao.returnDishByTitle(dish)!!)
                        kitchenApp.addDishToOrder(orderDao.returnOrderById(res)!!)
                        removeCountDishes(dish, 1)
                        println("Congratulation! You add $dish")
                    } else {
                        println("ERROR [This dish is not on menu]")
                    }
                } else {
                    println("ERROR [This dish is not defined]")
                }
            }
        } catch (ex: Exception) {
            println("ERROR [It was required to enter a number]")
        }
    }

    private fun cancelOrder() { // cancel cooking order
        print("Input ID order if you want to cancel it: ")
        try {
            val res = readln().toInt()
            if (orderDao.returnOrderById(res)!!.status == "cooking" && orderDao.returnOrderById(res)!! in orderDao.returnOrdersByUser(
                    user.login
                )
            ) {
                orderDao.setStatus(orderDao.returnOrderById(res)!!, "canceled")
                kitchenApp.cancelOrder(orderDao.returnOrderById(res)!!)
            }
        } catch (ex: Exception) {
            println("ERROR [It was required to enter a number]")
        }
    }

    private fun interactionWithReview() { // interaction with review
        println("Reviews")
        println("1. Create a review")
        println("2. Edit a review")
        println("3. Delete a review")
        print("Choose one of the actions: ")
        val choose = readln()
        when (choose) {
            "1" -> {
                println("Create a review")
                printOrders("paid")
                if (orderDao.returnOrdersByUser(user.login).none { order -> order.status == "paid" }) {
                    println("Place an order and pay for it to make a review!")
                } else {
                    print("Input ID: ")
                    try {
                        val otv = readln().toInt()
                        var coun = 1
                        if (orderDao.returnOrderById(otv)!! in orderDao.returnOrdersByUser(user.login)) {
                            if (orderDao.returnOrderById(otv) != null && orderDao.returnOrderById(otv)!!.status == "paid") {
                                for (dish in orderDao.returnOrderById(otv)!!.dishes.toList()) {
                                    println("${coun++}. Dish: ${dish.title}, price: ${dish.price}.")
                                }
                                print("Input title dish: ")
                                val dish = readln()
                                if (dishDao.returnDishByTitle(dish) in orderDao.returnOrderById(otv)!!.dishes
                                ) {
                                    if (reviewDao.getReviewUserAboutDished(user.login, dish) != null) {
                                        println("ERROR [the user has already left a review about this dish]")
                                        return
                                    }
                                    print("Input stars (1-5): ")
                                    try {
                                        val stars = readln().toInt()
                                        print("Input text: ")
                                        val text = readln()
                                        if (stars in 1..5) {
                                            if (text.length >= 20) {
                                                reviewDao.createReview(
                                                    dishDao.returnDishByTitle(dish)!!,
                                                    user.login,
                                                    text,
                                                    stars
                                                )
                                                println("Congratulation! You create new review!")
                                            } else {
                                                println("ERROR [The text must contain at least 20 characters]")
                                            }
                                        } else {
                                            println("ERROR [The number of stars should be from 1 to 5]")
                                        }
                                    } catch (ex: Exception) {
                                        println("ERROR [It was required to enter a number]")
                                    }
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        println("ERROR [It was required to enter a number]")
                    }
                }
            }
            "2" -> {
                println("Edit a review")
                printOrders("paid")
                if (orderDao.returnOrdersByUser(user.login).none { order -> order.status == "paid" }) {
                    println("Place an order and pay for it to edit a review!")
                } else {
                    print("Input title dish: ")
                    val dish = readln()
                    if (dishDao.returnDishByTitle(dish) == null) {
                        println("ERROR [This dish is not defined]")
                        return
                    }
                    if (reviewDao.getReviewUserAboutDished(user.login, dish) != null) {
                        print("Input stars (1-5): ")
                        try {
                            val stars = readln().toInt()
                            print("Input text: ")
                            val text = readln()
                            if (stars in 1..5) {
                                if (text.length >= 20) {
                                    reviewDao.editReview(reviewDao.getReviewUserAboutDished(user.login, dish)!!, stars, text)
                                    println("Congratulation! You edit the review about dish!")
                                } else {
                                    println("ERROR [The text must contain at least 20 characters]")
                                }
                            } else {
                                println("ERROR [The number of stars should be from 1 to 5]")
                            }
                        } catch (ex: Exception) {
                            println("ERROR [It was required to enter a number]")
                        }
                        return
                    } else {
                        println("ERROR [The user's review of this dish was not found]")
                    }

                }
            }
            "3" -> {
                println("Delete a review")
                printOrders("paid")
                if (orderDao.returnOrdersByUser(user.login).none { order -> order.status == "paid" }) {
                    println("Place an order and pay for it to edit a review!")
                } else {
                    print("Input title dish: ")
                    val dish = readln()
                    if (dishDao.returnDishByTitle(dish) == null) {
                        println("ERROR [This dish is not defined]")
                        return
                    }
                    if (reviewDao.getReviewUserAboutDished(user.login, dish) != null) {
                        print("Are you sure?(y/other): ")
                        val res = readln()
                        when (res) {
                            "y" -> {
                                reviewDao.deleteReview(reviewDao.getReviewUserAboutDished(user.login, dish)!!)
                                println("Congratulation! You delete your review!")
                            }
                            else -> {
                                println("Cancel!")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun printOrders(status: String) { // print information about orders by status
        var coun = 1
        for (order in orderDao.returnOrdersByStatus(status)) {
            val dishCountMap = mutableMapOf<String, Int>()
            for (dish in order.dishes) {
                val title = dish.title
                dishCountMap[title] = dishCountMap.getOrDefault(title, 0) + 1
            }
            if (order in orderDao.returnOrdersByUser(user.login)) {
                print("${coun++}. ID: ${order.id}, status: ${order.status}, dishes: ")
            }
            for ((dish, count) in dishCountMap) {
                print("$dish x$count, ")
            }
            println()
        }
        if (coun == 1) {
            println("Orders is empty!")
        }
    }

    private fun removeCountDishes(title : String, count : Int) { // change the value of the number of dishes
        menuDao.returnDishByTitle(title)!!.count -= count
        if (menuDao.returnDishByTitle(title)!!.count <= 0) {
            menuDao.deleteDishWithMenu(menuDao.returnDishByTitle(title)!!)
        }
    }
}