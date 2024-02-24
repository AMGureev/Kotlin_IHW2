package ru.hse.restaurant.dao

import org.mindrot.jbcrypt.BCrypt
import ru.hse.restaurant.entity.AdminEntity
import ru.hse.restaurant.entity.AccountEntity
import ru.hse.restaurant.entity.UserEntity

class InMemoryAccountDao : AccountDao {
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

    override fun authenticateAccount(inputPassword: String, account: AccountEntity): Boolean {
        return BCrypt.checkpw(inputPassword, account.password)
    }

    override fun findAccountByLogin(login: String): Boolean {
        val allAccounts = users + admins
        return allAccounts.any { it.login == login }
    }

    override fun saveAllAccounts() {
        TODO("Not yet implemented")
    }

    override fun fillingAccountsData() {
        TODO("Not yet implemented")
    }

    override fun returnAccountByLogin(login: String) : AccountEntity? {
        val allAccounts = users + admins
        return allAccounts.find { it.login == login }
    }

    override fun returnAllAccounts() : List<AccountEntity>{
        val allAccounts = users + admins
        return allAccounts.toList()
    }

}