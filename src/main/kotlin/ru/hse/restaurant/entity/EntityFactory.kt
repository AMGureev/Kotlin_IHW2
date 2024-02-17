package ru.hse.restaurant.entity

import ru.hse.restaurant.enums.Type

class EntityFactory {
    fun createPerson(type: Type, login : String, password : String): PersonEntity =
        when (type) {
            Type.ADMIN -> AdminEntity(password, login)
            Type.USER -> UserEntity(password, login)
        }
}