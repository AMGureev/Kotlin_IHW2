package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.*
import ru.hse.restaurant.entity.AdminEntity
import kotlin.system.exitProcess

class ConsoleController() : Controller {
    private val accountDao = InMemoryAccountDao()
    private val dishDao = InMemoryDishDao()
    private val menuDao = InMemoryMenuDao()
    private val orderDao = InMemoryOrderDao()
    private val reviewDao = InMemoryReviewDao()
    private val kitchenApp = KitchenApp()
    override fun launch() {
        printHelloTable()
    }

    private fun printHelloTable() {
        print("Choose one of the three actions:\n1. Sign in\n2. Sign up\n3. Exit program\nYour choose: ")
        var ans: Int = 0
        try {
            ans = readln().toInt()
        } catch (e: Exception) {
            println("Error!")
            printHelloTable()
        }
        when (ans) {
            1 -> {
                println("Sign in!")
                print("Input your login: ")
                val login = readln()
                print("Input your password: ")
                val password = readln()
                if (authenticateUser(login, password)) {
                    if (accountDao.returnAccountByLogin(login) is AdminEntity) {
                        val adminController = ConsoleControllerAdmin(
                            accountDao.returnAccountByLogin(login)!!,
                            this,
                            dishDao,
                            menuDao,
                            orderDao,
                            reviewDao,
                            accountDao,
                            kitchenApp
                        )
                        adminController.launch()
                    } else {
                        val userController = ConsoleControllerUser(
                            accountDao.returnAccountByLogin(login)!!,
                            this,
                            dishDao,
                            menuDao,
                            orderDao,
                            reviewDao,
                            kitchenApp
                        )
                        userController.launch()
                    }
                } else {
                    printHelloTable()
                }
            }

            2 -> {
                println("Sign up!")
                print("Input your new login: ")
                val login = readln()
                print("Input your new password: ")
                val password = readln()
                print("Are you an admin(y/n): ")
                val isAdmin = readln()
                when (isAdmin) {
                    "y" -> {
                        registerAccount(login, password, true)
                    }

                    "n" -> {
                        registerAccount(login, password, false)
                    }

                    else -> {
                        println("Error!")
                    }
                }
                saveAllInformationToJson()
                printHelloTable()
            }

            3 -> {
                exitProgram()
            }
        }
    }

    private fun registerAccount(name: String, password: String, isAdmin: Boolean) { // process register user
        if (accountDao.findAccountByLogin(name)) {
            println("An account with this username already exists!")
        } else {
            if (isAdmin) {
                accountDao.registerAdmin(name, password)
                println("The account with this username $name(admin) successfully creation!")
            } else {
                accountDao.registerUser(name, password)
                println("The account with this username $name successfully creation!")
            }
        }
    }

    private fun authenticateUser(
        name: String,
        inputPassword: String
    ): Boolean { // authenticate user : return true or false
        return if (accountDao.findAccountByLogin(name)) {
            if (accountDao.authenticateAccount(inputPassword, accountDao.returnAccountByLogin(name)!!)) {
                println("Access granted!")
                true
            } else {
                println("Password error!")
                false
            }
        } else {
            println("Error! - an account with that username does not exist")
            false
        }
    }

    fun saveAllInformationToJson() { // save all information about accounts to Json file
        accountDao.saveAllAccounts()
        dishDao.saveAllDishes()
        menuDao.saveAllMenu()
        reviewDao.saveAllReviews()
        orderDao.saveAllOrders()
    }

    fun initialFillingOfFiles() { // get information from json file
        accountDao.fillingAccountsData()
        dishDao.fillingDishesData()
        menuDao.fillingMenuData()
        reviewDao.fillingReviewsData()
        orderDao.fillingOrdersData()
    }

    fun exitProgram() {
        if (orderDao.returnOrdersByStatus("cooking").isNotEmpty()) {
            println("Attention! ${orderDao.returnOrdersByStatus("cooking").size} orders are cooking! If you exit, these orders aren't saving!")
            print("Exit or not?(EXIT/other): ")
            val otv = readln()
            when (otv) {
                "EXIT" -> {
                    saveAllInformationToJson()
                    println("Exit program! Goodbye!")
                    exitProcess(0)
                }

                else -> {
                    println("THX YOU")
                }
            }
        } else {
            saveAllInformationToJson()
            println("Exit program! Goodbye!")
            exitProcess(0)
        }
    }
}