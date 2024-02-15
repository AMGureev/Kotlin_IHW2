package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.AdminEntity
import ru.hse.restaurant.entity.PersonEntity
import ru.hse.restaurant.entity.UserEntity

class InMemoryPersonDao : PersonDao {
    private var admins = mutableListOf<AdminEntity>()
    private var users = mutableListOf<UserEntity>()
    override fun registerUser(login: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        users.add(UserEntity(hashedPassword, login))
    }

    override fun registerAdmin(login: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        admins.add(AdminEntity(hashedPassword, login))
    }

    override fun authenticatePerson(inputPassword: String, person : PersonEntity): Boolean {
        return BCrypt.checkpw(inputPassword, person.password)
    }

    override fun findAccountByLogin(login: String): Boolean {
        return users.any { it.login == login }
    }

    override fun saveAllAccounts() {
        TODO("Not yet implemented")
    }

    override fun fillingAccountsData() {
        TODO("Not yet implemented")
    }
}