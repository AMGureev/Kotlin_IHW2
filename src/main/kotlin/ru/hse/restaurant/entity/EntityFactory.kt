package ru.hse.restaurant.entity

import ru.hse.restaurant.enums.Type

class EntityFactory {
    fun createAccount(type: Type, login : String, password : String): AccountEntity =
        when (type) {
            Type.ADMIN -> AdminEntity(password, login)
            Type.USER -> UserEntity(password, login)
        }
}