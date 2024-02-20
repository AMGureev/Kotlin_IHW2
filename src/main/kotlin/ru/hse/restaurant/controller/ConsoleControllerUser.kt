package ru.hse.restaurant.controller

import ru.hse.restaurant.dao.InMemoryDishDao
import ru.hse.restaurant.dao.InMemoryMenuDao
import ru.hse.restaurant.dao.InMemoryOrderDao
import ru.hse.restaurant.dao.InMemoryReviewDao
import kotlin.system.exitProcess

class ConsoleControllerUser : Controller{
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
        println("2. Make an order")
        println("3. Interaction with my orders")
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
                // делает заказ
            }
            "3"-> {
                println("3. Interaction with my orders")
                println("Choose one of the actions:")
                println("1. All orders")
                println("2. Cooking orders")
                println("3. Orders awaiting payment")
                print("Enter your choose: ")
                val otv = readln()
                when (otv) {
                    "1"-> {

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