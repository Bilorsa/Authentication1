package com.example.authentication1.ui

import android.app.DatePickerDialog
import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ViewTotalsActivity : ComponentActivity() {

    private lateinit var dao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = AppDatabase.getDatabase(this).budgetDao()

        setContent {
            ViewTotalsScreen(dao)
        }
    }
}

@Composable
fun ViewTotalsScreen(dao: BudgetDao) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var endDate by remember { mutableStateOf(System.currentTimeMillis()) }

    val categories by dao.getAllCategories().collectAsState(initial = emptyList())

    var totals by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

        Button(onClick = {
            scope.launch(Dispatchers.IO) {

                val resultMap = mutableMapOf<String, Double>()

                for (category in categories) {
                    val total = dao.getTotalSpentByCategory(
                        category.id,
                        startDate,
                        endDate
                    ) ?: 0.0

                    resultMap[category.name] = total
                }

                withContext(Dispatchers.Main) {
                    totals = resultMap
                }
            }
        }) {
            Text("Calculate Totals")
        }

        Spacer(modifier = Modifier.height(16.dp))

        totals.forEach { (category, total) ->
            Text("$category: R$total")
        }
    }
}
