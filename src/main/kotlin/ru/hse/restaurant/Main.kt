package org.example.ru.hse.restaurant

import ru.hse.restaurant.controller.ConsoleController

fun main() {
    val console = ConsoleController()
    console.initialFillingOfFiles()
    console.launch()
}