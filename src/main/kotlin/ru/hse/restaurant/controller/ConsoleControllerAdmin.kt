package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.*

class ConsoleControllerAdmin(
    admin: AccountEntity,
    private val console: ConsoleController,
    private val dishDao: InMemoryDishDao,
    private val menuDao: InMemoryMenuDao,
    private val orderDao: InMemoryOrderDao,
    private val reviewDao: InMemoryReviewDao,
    private val accountDao: InMemoryAccountDao,
    private val kitchenApp: KitchenApp
) : Controller {
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
        val ans = readln()
        when (ans) {
            "1" -> {
                interactionWithDishes()
            }

            "2" -> {
                getStats()
            }

            "3" -> {
                infoAboutOrders()
            }

            "4" -> {
                infoAboutAccounts()
            }

            "5" -> {
                println("Sign out!")
                console.launch()
            }

            "6" -> {
                this.console.exitProgram()
            }
        }
        printMainTable()
    }

    private fun createNewDish(
        title: String,
        price: Int,
        duration: Int,
        weight: Double
    ) {
        if (!checkCorrectInput(title, price, duration, weight)) {
            return
        }
        if (dishDao.returnDishByTitle(title) == null) {
            dishDao.createDish(DishEntity(title, price, duration, weight, mutableListOf<ReviewEntity>()))
            print("Add this dish to menu?(y/other): ")
            val res = readln()
            when (res) {
                "y" -> {
                    menuDao.addDishToMenu(dishDao.returnDishByTitle(title)!!)
                    println("Congratulation! This dish on menu!")
                }

                else -> {
                    println("Okay! Create new dish (not add to menu)")
                }
            }
        } else {
            println("ERROR [A dish with that name already exists]")
        }
    }

    private fun deleteDishOnMenu(title: String) {
        if (dishDao.returnDishByTitle(title) == null) {
            println("Error!")
            return
        } else {
            if (orderDao.returnOrdersByStatus("cooking")
                    .filter { order -> dishDao.returnDishByTitle(title) in order.dishes }.isEmpty()
            ) {
                print("Are you sure?(y/other): ")
                val res = readln()
                when (res) {
                    "y" -> {
                        dishDao.deleteDish(dishDao.returnDishByTitle(title)!!)
                        println("Congratulation!")
                    }

                    else -> {
                        println("Cancel!")
                    }
                }
            } else {
                println("ERROR [At the moment, it is impossible to remove a dish from the menu, since the chefs are preparing it]")
            }
        }
    }

    private fun addDishToMenu(titleDish: String) {
        if (dishDao.returnDishByTitle(titleDish) == null) {
            println("ERROR [This dish is not defined]")
            return
        }
        if (dishDao.returnDishByTitle(titleDish)!! in menuDao.returnAllDishes()) {
            println("ERROR [This dish already on menu]")
            return
        }
        menuDao.addDishToMenu(dishDao.returnDishByTitle(titleDish)!!)
        println("Congratulation!")
    }

    private fun editInfoAboutDish(
        oldTitle: String,
        newTitle: String,
        newPrice: Int,
        newWeight: Double,
        newDuration: Int
    ) {
        var newTitle = newTitle
        var newPrice = newPrice
        var newWeight = newWeight
        var newDuration = newDuration
        if (newTitle.isEmpty()) {
            newTitle = oldTitle
        }
        if (!checkCorrectInput(newTitle, newPrice, newDuration, newWeight)) {
            return
        }
        if (dishDao.returnDishByTitle(oldTitle) == null) {
            println("ERROR [This dish is not defined]")
            return
        }
        if (newWeight == 0.0) {
            newWeight = dishDao.returnDishByTitle(oldTitle)!!.weight
        }
        if (newPrice == 0) {
            newPrice = dishDao.returnDishByTitle(oldTitle)!!.price
        }
        if (newDuration == 0) {
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
        for (review in reviewDao.getReviewsAboutDished(dish)) {
            cou += review.stars
        }
        println("Average stars : ${cou / reviewDao.getReviewsAboutDished(dish).size}.")
    }

    private fun getAllReview(dish: DishEntity) {
        if (reviewDao.getReviewsAboutDished(dish).isEmpty()) {
            println("List is empty!")
            return
        }
        var cou = 1
        for (review in reviewDao.getReviewsAboutDished(dish)) {
            println("${cou++}. Name: ${review.userName}, stars: ${review.stars} ,text: ${review.text}")
        }
    }

    private fun getAverageStarsAllDishes() {
        var averageStars = 0
        for (elem in reviewDao.getAllReviews()) {
            averageStars += elem.stars
        }
        println("Average stars all dishes : ${averageStars / reviewDao.getAllReviews().size}.")
    }

    private fun returnRevenue() {
        println("Total revenue : ${orderDao.returnRevenue()}$.")
    }

    private fun checkCorrectInput(title: String, price: Int, duration: Int, weight: Double): Boolean {
        if (title.length < 5) {
            println("ERROR [The length must be at least 5]")
            return false
        }
        if (price <= 0) {
            println("ERROR [The price cannot be negative]")
            return false
        }
        if (duration <= 0) {
            println("ERROR [The duration cannot be negative]")
            return false
        }
        if (weight <= 0) {
            println("ERROR [The weight cannot be negative]")
            return false
        }
        return true
    }

    private fun interactionWithDishes() {
        println("Interaction with dishes!")
        println("1. Create new dish")
        println("2. Delete dish")
        println("3. Check all dishes")
        println("4. Add a dish to the menu")
        println("5. Edit info about dish")
        print("Choose one of the actions: ")
        val ans = readln()
        when (ans) {
            "1" -> {
                println("Create new dish")
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
                } catch (ex: Exception) {
                    println("ERROR [It was required to enter a number]")
                }
            }

            "2" -> {
                println("Delete dish!")
                print("Input dish's title: ")
                val title = readln()
                deleteDishOnMenu(title)
                printMainTable()
            }

            "3" -> {
                var cou = 1
                print("Check all dishes or only in menu(y/n)?: ")
                val res = readln()
                when (res) {
                    "y" -> {
                        if (dishDao.returnAllDishes().isEmpty()) {
                            println("Zero dishes...")
                        }
                        println("All dishes:")
                        for (dish in dishDao.returnAllDishes()) {
                            println("${cou++}. Title: ${dish.title}, price: ${dish.price}$, weight: ${dish.weight}, duration: ${dish.duration}")
                            // добавить пункт в меню или нет
                        }
                    }

                    "n" -> {
                        if (menuDao.returnAllDishes().isEmpty()) {
                            println("The menu is empty!")
                            return
                        }
                        println("The menu's dishes:")
                        for (dish in menuDao.returnAllDishes()) {
                            println("${cou++}. Title: ${dish.title}, price: ${dish.price}\$, weight: ${dish.weight}, duration: ${dish.duration}")
                        }
                    }

                    else -> {
                        println("ERROR [Expected input y or n]")
                    }
                }
            }

            "4" -> {
                println("Add a dish to the menu!")
                print("Input dish's title: ")
                val title = readln()
                addDishToMenu(title)
            }

            "5" -> {
                println("Edit info about dish!")
                print("Input dish's title: ")
                val title = readln()
                print("Input new dish's title(if you want to leave, input nothing: ")
                var newTitle = readln()
                println("Input new dish's price(if you want to leave, input nothing or zero: ")
                var newPrice = readln()
                if (newPrice.isEmpty()) {
                    newPrice = "0"
                }
                println("Input new dish's weight(if you want to leave, input nothing or zero: ")
                var newWeight = readln()
                if (newWeight.isEmpty()) {
                    newWeight = "0"
                }
                println("Input new dish's duration(if you want to leave, input nothing or zero: ")
                var newDuration = readln()
                if (newDuration.isEmpty()) {
                    newDuration = "0"
                }
                editInfoAboutDish(title, newTitle, newPrice.toInt(), newWeight.toDouble(), newDuration.toInt())
            }
        }
    }

    private fun getStats() {
        println("Get stats!")
        println("1. Stats about dish")
        println("2. Average dishes rating")
        println("3. Revenue")
        print("Input your choose: ")
        val ans = readln()
        when (ans) {
            "1" -> {
                print("Input dish's title: ")
                val title = readln()
                if (dishDao.returnDishByTitle(title) != null) {
                    println("1. Get average stars")
                    println("2. Get all reviews")
                    print("Input your choose: ")
                    val otv = readln()
                    when (otv) {
                        "1" -> {
                            getAverageStars(dishDao.returnDishByTitle(title)!!)
                        }

                        "2" -> {
                            getAllReview(dishDao.returnDishByTitle(title)!!)
                        }

                        else -> {
                            println("Go to main table...")
                        }
                    }
                } else {
                    println("ERROR [This dish is not defined]")
                }
            }

            "2" -> {
                getAverageStarsAllDishes()
            }

            "3" -> {
                returnRevenue()
            }

            else -> {
                println("Go to main table...")
            }
        }
    }

    private fun infoAboutOrders() {
        println("1. In process - 'cooking'")
        println("2. Wait to pay - 'ready'")
        println("3. Cancelled by the user - 'canceled'")
        println("4. Paid for by the user - 'paid'")
        print("Select one of the order's status: ")
        val res = readln()
        when (res) {
            "1" -> {
                val status = "cooking"
                println("All cooking orders:")
                printOrders(status)
            }

            "2" -> {
                val status = "ready"
                println("All ready orders:")
                printOrders(status)
            }

            "3" -> {
                val status = "canceled"
                println("All canceled orders:")
                printOrders(status)
            }

            "4" -> {
                val status = "paid"
                println("All paid orders:")
                printOrders(status)
            }

            else -> {
                println("Go to main table...")
            }
        }
    }

    private fun infoAboutAccounts() {
        println("Info about accounts")
        var coun = 1
        for (acc in accountDao.returnAllAccounts()) {
            println(
                "${coun++}. Login: ${acc.login}, rule: ${
                    if (acc is AdminEntity) {
                        "admin"
                    } else {
                        "user"
                    }
                }.${
                    if (acc is AdminEntity) {
                        ""
                    } else {
                        "\n" +
                                "Count of orders: ${orderDao.returnOrdersByUser(acc.login).size}"
                    }
                }"
            )
        }
    }

    private fun printOrders(status: String) {
        var coun = 1
        for (order in orderDao.returnOrdersByStatus(status)) {
            println("${coun++}. ID: ${order.id}, person: ${order.person}, dishes: ${order.dishes}")
        }
        if (coun == 1) {
            println("Zero paid orders")
        }
    }
}