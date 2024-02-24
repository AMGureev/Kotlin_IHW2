package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.AccountEntity

interface AccountDao {
    fun registerUser(login: String, password: String) // user registration
    fun registerAdmin(login: String, password: String) // admin registration
    fun authenticateAccount(inputPassword: String, account: AccountEntity): Boolean // authenticate user
    fun findAccountByLogin(login: String) : Boolean // search for account by login
    fun saveAllAccounts() // save all accounts in json file
    fun fillingAccountsData() // data recovery from file
    fun returnAccountByLogin(login : String) : AccountEntity?
    fun returnAllAccounts() : List<AccountEntity>
}