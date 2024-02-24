package ru.hse.restaurant.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.OrderEntity
import ru.hse.restaurant.entity.UserEntity
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.io.path.Path

class InMemoryOrderDao : OrderDao {
    private var orders = mutableListOf<OrderEntity>()
    private val directoryPath = "orders"
    private val fileName = "orders.json"
    private var lastId = 0
    override fun createOrder(person: UserEntity, dishes: List<DishEntity>): OrderEntity {
        val time = LocalDateTime.now()
        val order = OrderEntity(lastId, person.login, "cooking", dishes, time)
        orders.add(order)
        lastId += 1
        return order
    }


    override fun cancelOrder(order: OrderEntity) {
        orders.remove(order)
    }

    /*
    fun processOrder(order: OrderEntity) {
        val threads = order.dishes.map{ dish->
            Thread {
                cookDish(dish)
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        order.status = "ready"
    }
     */
    override fun getStatus(order: OrderEntity): String {
        return order.status
    }
    /*
    private fun cookDish(dish : DishEntity) {
        Thread.sleep(dish.duration.toLong())
    }
     */

    override fun payOrder(order: OrderEntity) {
        order.status = "paid"
    }

    override fun getCostOfOrder(order: OrderEntity): Int {
        var sum = 0
        for (i in 0..order.dishes.size) {
            sum += order.dishes[i].price
        }
        return sum
    }

    override fun returnOrderById(id: Int): OrderEntity? {
        return orders.find { it.id == id }
    }

    override fun setStatus(order: OrderEntity, status: String) {
        order.status = status
    }

    override fun returnOrdersByStatus(status: String): List<OrderEntity> {
        return orders.filter { order ->
            order.status == status
        }
    }

    override fun returnRevenue(): Int {
        var revenue = 0
        for (elem in returnOrdersByStatus("paid")) {
            for (dish in elem.dishes) {
                revenue += dish.price
            }
        }
        return revenue
    }

    override fun returnOrdersByUser(login: String): List<OrderEntity> {
        return orders.filter { order ->
            order.person == login
        }
    }

    override fun addDishToOrder(order: OrderEntity, dish: DishEntity) {
        order.dishes.addLast(dish)
    }

    override fun saveAllOrders() {
        File(directoryPath).mkdirs()
        val file = Path(directoryPath, fileName).toFile()
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, orders.filter { order -> order.status != "cooking" })
    }

    override fun returnAllOrders(): List<OrderEntity> {
        return orders.toList()
    }

    override fun fillingOrdersData() {
        File(directoryPath).mkdirs()
        val file = File(directoryPath, fileName)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        orders = mapper.readValue<MutableList<OrderEntity>>(file.readText())
        lastId = orders.size
    }
}