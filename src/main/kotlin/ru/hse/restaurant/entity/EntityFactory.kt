package ru.hse.restaurant.entity

class EntityFactory {
    enum class Type {ADMIN, USER}
    fun createPerson(type: Type): PersonEntity =
        when (type) {
            Type.ADMIN -> AdminEntity()
            Type.USER -> UserEntity()
        }
}