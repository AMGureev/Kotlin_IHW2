package ru.hse.restaurant.entity

class EntityFactory {
    enum class Type {ADMIN, USER}
    fun createPerson(type: Type, login : String, password : String): PersonEntity =
        when (type) {
            Type.ADMIN -> AdminEntity(password, login)
            Type.USER -> UserEntity(password, login)
        }
}