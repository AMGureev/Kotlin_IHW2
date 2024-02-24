package ru.hse.restaurant.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.hse.restaurant.entity.DishEntity
import ru.hse.restaurant.entity.MenuEntity
import java.io.File
import kotlin.io.path.Path

class InMemoryMenuDao : MenuDao{
    private var dishes = mutableListOf<DishEntity>()
    private val directoryPath = "menu"
    private val fileName = "menu.json"
    override fun addDishToMenu(dish: DishEntity) {
        dishes.add(dish)
    }

    override fun deleteDishWithMenu(dish: DishEntity) {
        dishes.remove(dish)
    }

    override fun returnAllDishes(): List<DishEntity> {
        return dishes.toList()
    }

    override fun returnDishesByTitle(title : String) : DishEntity? {
        return dishes.find { it.title == title }
    }
    override fun saveAllMenu() {
        File(directoryPath).mkdirs()
        val file = Path(directoryPath, fileName).toFile()
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, dishes)
    }

    override fun fillingMenuData() {
        File(directoryPath).mkdirs()
        val file = File(directoryPath, fileName)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        dishes = mapper.readValue<MutableList<DishEntity>>(file.readText())
    }
}