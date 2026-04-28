package com.example.authentication1.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.local.AppDatabase
import com.example.budgettracker.data.local.dao.BudgetDao
import com.example.budgettracker.data.local.entities.BudgetGoal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SetGoalActivity : ComponentActivity() {

    private lateinit var dao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = AppDatabase.getDatabase(this).budgetDao()

        setContent {
            SetGoalScreen(dao)
        }
    }
}

@Composable
fun SetGoalScreen(dao: BudgetDao) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var minGoal by remember { mutableStateOf("") }
    var maxGoal by remember { mutableStateOf("") }

    val currentMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        .format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Month: $currentMonth")

        OutlinedTextField(
            value = minGoal,
            onValueChange = { minGoal = it },
            label = { Text("Minimum Monthly Goal") }
        )

        OutlinedTextField(
            value = maxGoal,
            onValueChange = { maxGoal = it },
            label = { Text("Maximum Monthly Goal") }
        )

        Button(onClick = {

            val min = minGoal.toDoubleOrNull()
            val max = maxGoal.toDoubleOrNull()

            if (min == null || max == null) {
                Toast.makeText(context, "Enter valid numbers", Toast.LENGTH_SHORT).show()
                return@Button
            }

            scope.launch(Dispatchers.IO) {
                dao.insertBudgetGoal(
                    BudgetGoal(
                        monthYear = currentMonth,
                        minGoal = min,
                        maxGoal = max
                    )
                )
            }

            Toast.makeText(context, "Goal Saved", Toast.LENGTH_SHORT).show()

        }) {
            Text("Save Goal")
        }
    }
}
