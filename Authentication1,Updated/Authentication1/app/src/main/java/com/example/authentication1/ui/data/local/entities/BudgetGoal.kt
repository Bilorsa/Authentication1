package com.example.budgettracker.data.local.entities

import androidx.room.Entity

import androidx.room.PrimaryKey
@Entity(tableName = "budget_goals")
data class BudgetGoal(
    @PrimaryKey val monthYear: String,
    val minGoal: Double,
    val maxGoal: Double
)