package ru.hse.restaurant.controller

import ru.hse.restaurant.RestaurantApp

class ConsoleControllerAdmin : Controller {
    override fun launch() {
        TODO("Not yet implemented")
    }
    fun printMainTable() {
        println("Main admin table")
        println("Choose one of the actions:")
        println("1. Edit menu")
        println("2. Get stats")
        println("3. Exit program")
        print("Enter your choose: ")
        val ans : Int = 0

    }
}