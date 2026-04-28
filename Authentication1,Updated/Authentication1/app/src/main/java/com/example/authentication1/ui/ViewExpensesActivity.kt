package com.example.authentication1.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.budgettracker.data.local.AppDatabase
import com.example.budgettracker.data.local.dao.BudgetDao
import java.util.*

class ViewExpensesActivity : ComponentActivity() {

    private lateinit var dao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = AppDatabase.getDatabase(this).budgetDao()

        setContent {
            ViewExpensesScreen(dao)
        }
    }
}

@Composable
fun ViewExpensesScreen(dao: BudgetDao) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf(System.currentTimeMillis()) }

    val expenses by dao.getExpensesByPeriod(startDate, endDate)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(onClick = {
            DatePickerDialog(
                context,
                { _, y, m, d ->
                    calendar.set(y, m, d)
                    startDate = calendar.timeInMillis
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text("Select Start Date")
        }

        Button(onClick = {
            DatePickerDialog(
                context,
                { _, y, m, d ->
                    calendar.set(y, m, d)
                    endDate = calendar.timeInMillis
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text("Select End Date")
        }

        LazyColumn {
            items(expenses) { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Description: ${expense.description}")
                        Text("Amount: ${expense.amount}")
                        Text("Time: ${expense.startTime} - ${expense.endTime}")

                        if (expense.photoUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(expense.photoUri),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
