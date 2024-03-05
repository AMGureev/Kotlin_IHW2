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
        println("5. Get info about revenue")
        println("6. Sign out")
        println("7. Exit program")
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
                returnRevenue()
            }
            "6" -> {
                println("Sign out!")
                console.launch()
            }

            "7" -> {
                this.console.exitProgram()
            }
        }
        printMainTable()
    }

    private fun createNewDish(
        title: String,
        price: Double,
        duration: Int,
        weight: Double
    ) {
        if (!checkCorrectInput(title, price, duration, weight)) {
            return
        }
        if (dishDao.returnDishByTitle(title) == null) {
            dishDao.createDish(DishEntity(title, price, duration, weight, 0,mutableListOf<ReviewEntity>()))
            println("Congratulation! Create new dish!")
            print("Add this dish to menu?(y/other): ")
            val res = readln()
            when (res) {
                "y" -> {
                    print("Enter the number of available dishes: ")
                    try {
                        val cou = readln().toInt()
                        if (0 < cou ){
                            dishDao.returnDishByTitle(title)!!.count += cou
                            menuDao.addDishToMenu(dishDao.returnDishByTitle(title)!!)
                            println("Congratulation! This dish on menu!")
                        } else {
                            println("ERROR [The quantity cannot be negative or zero]")
                        }
                    } catch (ex: Exception) {
                        println("ERROR [Invalid value]")
                    }
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
            println("ERROR [A dish with that name not exists]")
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
        if (dishDao.returnDishByTitle(titleDish)!!.count != 0) {
            menuDao.addDishToMenu(dishDao.returnDishByTitle(titleDish)!!)
            println("Congratulation! This dish on menu!")
            return
        }
        print("Enter the number of available dishes: ")
        try {
            val cou = readln().toInt()
            if (0 < cou ){
                dishDao.returnDishByTitle(titleDish)!!.count += cou
                menuDao.addDishToMenu(dishDao.returnDishByTitle(titleDish)!!)
                println("Congratulation! This dish on menu!")
            } else {
                println("ERROR [The quantity cannot be negative or zero]")
            }
        } catch (ex: Exception) {
            println("ERROR [Invalid value]")
        }
    }

    private fun editInfoAboutDish(
        oldTitle: String,
        newTitle: String,
        newPrice: Double,
        newCount: Int,
        newWeight: Double,
        newDuration: Int
    ) {
        var newTitle = newTitle
        var newPrice = newPrice
        var newCount = newCount
        var newWeight = newWeight
        var newDuration = newDuration
        if (newTitle.isNotEmpty() && newTitle.length < 5) {
            println("ERROR [The length must be at least 5]")
            return
        }
        if (newTitle.isEmpty()) {
            newTitle = oldTitle
        }
        if (newCount == 0) {
            newCount = dishDao.returnDishByTitle(oldTitle)!!.count
        }
        if (newWeight == 0.0) {
            newWeight = dishDao.returnDishByTitle(oldTitle)!!.weight
        }
        if (newPrice == 0.0) {
            newPrice = dishDao.returnDishByTitle(oldTitle)!!.price
        }
        if (newDuration == 0) {
            newDuration = dishDao.returnDishByTitle(oldTitle)!!.duration
        }
        dishDao.editDish(dishDao.returnDishByTitle(oldTitle)!!, newTitle, newPrice, newCount, newDuration, newWeight)
        println("Congratulation!")
    }

    private fun getAverageStars(dish: DishEntity) {
        if (reviewDao.getReviewsAboutDished(dish).isEmpty()) {
            println("Dishes are not appreciated!")
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
            println("Dishes are not appreciated!")
            return
        }
        var cou = 1
        for (review in reviewDao.getReviewsAboutDished(dish)) {
            println("${cou++}. Name: ${review.userName}, stars: ${review.stars} ,text: ${review.text}")
        }
    }

    private fun getAverageStarsAllDishes() {
        if (reviewDao.getAllReviews().isEmpty()) {
            println("It is impossible to calculate the average score!")
            return
        }
        var averageStars = 0
        for (elem in reviewDao.getAllReviews()) {
            averageStars += elem.stars
        }
        println("Average stars all dishes : ${averageStars / reviewDao.getAllReviews().size}.")
    }

    private fun returnRevenue() {
        println("Total revenue : ${orderDao.returnRevenue()}$.")
    }

    private fun checkCorrectInput(title: String, price: Double, duration: Int, weight: Double): Boolean {
        var count = 0
        if (title.length < 5) {
            println("ERROR [The length must be at least 5]")
            count += 1
        }
        if (price <= 0) {
            println("ERROR [The price cannot be negative]")
            count += 1
        }
        if (duration <= 0) {
            println("ERROR [The duration cannot be negative]")
            count += 1
        }
        if (weight <= 0) {
            println("ERROR [The weight cannot be negative]")
            count += 1
        }
        if (count > 1) {
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
                    val price = readln().toDouble()
                    print("Input duration (sec): ")
                    val duration = readln().toInt() * 1000
                    print("Input weight (grams): ")
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
                print("Check all dishes (input 'y') or only in menu (input 'n')?: ")
                val res = readln()
                when (res) {
                    "y" -> {
                        if (dishDao.returnAllDishes().isEmpty()) {
                            println("Zero dishes...")
                        }
                        println("All dishes:")
                        for (dish in dishDao.returnAllDishes()) {
                            println("${cou++}. Title: ${dish.title}, price: ${dish.price}$, count: ${dish.count}, weight: ${dish.weight} gram, duration: ${dish.duration} sec")
                        }
                    }

                    "n" -> {
                        if (menuDao.returnAllDishes().isEmpty()) {
                            println("The menu is empty!")
                            return
                        }
                        println("The menu's dishes:")
                        for (dish in menuDao.returnAllDishes()) {
                            println("${cou++}. Title: ${dish.title}, price: ${dish.price}$, count: ${dish.count}, weight: ${dish.weight} gram, duration: ${dish.duration} sec")
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
                if (dishDao.returnDishByTitle(title) == null) {
                    println("ERROR [This dish is not defined]")
                    return
                }
                print("Input new dish's title(If you want to keep the value, input nothing: ")
                var newTitle = readln()
                print("Input new dish's price(If you want to keep the value, input nothing or zero: ")
                var newPrice = readln()
                if (newPrice.isEmpty()) {
                    newPrice = "0"
                }
                try {
                    val p = newPrice.toDouble()
                    if (p < 0) {
                        println("ERROR [The price cannot be negative]")
                        return
                    }
                } catch (ex: Exception) {
                    println("ERROR [No number entered]")
                    return
                }
                print("Input new dish's count(If you want to keep the value, input nothing or zero: ")
                var newCount = readln()
                if (newCount.isEmpty()) {
                    newCount = "0"
                }
                try {
                    val p = newCount.toInt()
                    if (p < 0) {
                        println("ERROR [The count cannot be negative]")
                        return
                    }
                } catch (ex: Exception) {
                    println("ERROR [No number entered]")
                    return
                }
                print("Input new dish's weight(If you want to keep the value, input nothing or zero: ")
                var newWeight = readln()
                if (newWeight.isEmpty()) {
                    newWeight = "0"
                }
                try {
                    val p = newWeight.toDouble()
                    if (p < 0) {
                        println("ERROR [The weight cannot be negative]")
                        return
                    }
                } catch (ex: Exception) {
                    println("ERROR [No number entered]")
                    return
                }
                print("Input new dish's duration(If you want to keep the value, input nothing or zero: ")
                var newDuration = readln()
                if (newDuration.isEmpty()) {
                    newDuration = "0"
                }
                try {
                    val p = newDuration.toInt()
                    if (p < 0) {
                        println("ERROR [The duration cannot be negative]")
                        return
                    }
                } catch (ex: Exception) {
                    println("ERROR [No number entered]")
                    return
                }
                editInfoAboutDish(title, newTitle, newPrice.toDouble(), newCount.toInt(), newWeight.toDouble(), newDuration.toInt() * 1000)
            }
        }
    }

    private fun getStats() {
        println("Get stats!")
        println("1. Stats about dish")
        println("2. Average dishes rating")
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
            val dishCountMap = mutableMapOf<String, Int>()
            for (dish in order.dishes) {
                val title = dish.title
                dishCountMap[title] = dishCountMap.getOrDefault(title, 0) + 1
            }
            print("${coun++}. ID: ${order.id}, person: ${order.person}, dishes: ")
            for ((dish, count) in dishCountMap) {
                print("$dish x$count, ")
            }
            println()
        }
        if (coun == 1) {
            println("Zero $status orders")
        }
    }
}