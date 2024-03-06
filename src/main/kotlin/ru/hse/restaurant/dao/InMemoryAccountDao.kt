package ru.hse.restaurant.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.mindrot.jbcrypt.BCrypt
import ru.hse.restaurant.entity.AdminEntity
import ru.hse.restaurant.entity.AccountEntity
import ru.hse.restaurant.entity.EntityFactory
import ru.hse.restaurant.entity.UserEntity
import ru.hse.restaurant.enums.Type
import java.io.File
import kotlin.io.path.Path

class InMemoryAccountDao : AccountDao {
    private val entityFactory = EntityFactory()
    private val directoryPath = "accounts"
    private val fileAdminsNames = "admins.json"
    private val fileUsersNames = "users.json"
    private var admins = mutableListOf<AdminEntity>()
    private var users = mutableListOf<UserEntity>()
    override fun registerUser(login: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        users.add(entityFactory.createAccount(Type.USER, login, hashedPassword) as UserEntity)
    }

    override fun registerAdmin(login: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        admins.add(entityFactory.createAccount(Type.ADMIN, login, hashedPassword) as AdminEntity)
    }

    override fun authenticateAccount(inputPassword: String, account: AccountEntity): Boolean {
        return BCrypt.checkpw(inputPassword, account.password)
    }

    override fun findAccountByLogin(login: String): Boolean {
        val allAccounts = users + admins
        return allAccounts.any { it.login == login }
    }

    override fun returnAccountByLogin(login: String): AccountEntity? {
        val allAccounts = users + admins
        return allAccounts.find { it.login == login }
    }

    override fun returnAllAccounts(): List<AccountEntity> {
        val allAccounts = users + admins
        return allAccounts.toList()
    }

    override fun saveAllAccounts() {
        File(directoryPath).mkdirs()
        var file = Path(directoryPath, fileAdminsNames).toFile()
        var mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, admins)
        file = Path(directoryPath, fileUsersNames).toFile()
        mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(file, users)
    }

    override fun fillingAccountsData() {
        File(directoryPath).mkdirs()
        var file = File(directoryPath, fileAdminsNames)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        var mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        admins = mapper.readValue<MutableList<AdminEntity>>(file.readText())
        file = File(directoryPath, fileUsersNames)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        users = mapper.readValue<MutableList<UserEntity>>(file.readText())
    }
}