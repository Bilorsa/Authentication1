package com.example.budgettracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.local.entities.BudgetGoal
import com.example.budgettracker.data.local.entities.Category
import com.example.budgettracker.data.local.entities.Expense
import com.example.budgettracker.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    // --- USER LOGIC ---
    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    // --- EXPENSE LOGIC ---
    @Insert
    suspend fun insertExpense(expense: Expense)

    /**
     * Requirement: "view the total amount of money spent on each category during a user-selectable period"
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :catId AND date BETWEEN :start AND :end")
    suspend fun getTotalSpentByCategory(catId: Int, start: Long, end: Long): Double?

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end")
    fun getExpensesByPeriod(start: Long, end: Long): Flow<List<Expense>>

    // --- CATEGORY LOGIC ---
    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    // --- BUDGET GOAL LOGIC ---
    /**
     * Requirement: "set a minimum monthly goal... as well as a maximum goal"
     * We use REPLACE so if the user updates their goal for the same month, it overwrites the old one.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetGoal(goal: BudgetGoal)

    @Query("SELECT * FROM budget_goals WHERE monthYear = :monthYear LIMIT 1")
    suspend fun getGoalForMonth(monthYear: String): BudgetGoal?
}