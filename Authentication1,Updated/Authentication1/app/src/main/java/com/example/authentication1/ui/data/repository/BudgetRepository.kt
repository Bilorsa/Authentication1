package com.example.budgettracker.data.repository

import com.example.budgettracker.data.local.dao.BudgetDao
import com.example.budgettracker.data.local.entities.Expense
import com.example.budgettracker.data.local.entities.User

class BudgetRepository(private val budgetDao: BudgetDao) {

    // Users
    suspend fun register(user: User) = budgetDao.registerUser(user)
    suspend fun login(u: String, p: String) = budgetDao.login(u, p)

    // Expenses
    val allCategories = budgetDao.getAllCategories()

    suspend fun addExpense(expense: Expense) = budgetDao.insertExpense(expense)

    fun getExpensesForPeriod(start: Long, end: Long) =
        budgetDao.getExpensesByPeriod(start, end)
}