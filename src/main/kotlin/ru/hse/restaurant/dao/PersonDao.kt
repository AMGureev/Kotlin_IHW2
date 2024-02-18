package ru.hse.restaurant.dao

import ru.hse.restaurant.entity.PersonEntity

interface PersonDao {
    fun registerUser(login: String, password: String) // user registration
    fun registerAdmin(login: String, password: String) // admin registration
    fun authenticatePerson(inputPassword: String, person: PersonEntity): Boolean // authenticate user
    fun findAccountByLogin(login: String) : Boolean // search for account by login
    fun saveAllAccounts() // save all accounts in json file
    fun fillingAccountsData() // data recovery from file
    fun returnAccountByLogin(login : String) : PersonEntity?
}