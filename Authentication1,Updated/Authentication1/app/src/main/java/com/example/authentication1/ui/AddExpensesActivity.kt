package com.example.authentication1.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budgettracker.data.local.AppDatabase
import com.example.budgettracker.data.local.dao.BudgetDao
import com.example.budgettracker.data.local.entities.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddExpensesActivity : ComponentActivity() {

    private lateinit var dao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = AppDatabase.getDatabase(this).budgetDao()

        setContent {
            AddExpenseScreen(dao)
        }
    }
}


@Composable
fun AddExpenseScreen(dao: BudgetDao) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var photoUri by remember { mutableStateOf<String?>(null) }

    val categories by dao.getAllCategories().collectAsState(initial = emptyList())

    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Button(onClick = {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.timeInMillis
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text("Select Date")
        }

        Button(onClick = {
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    startTime = "$hour:$minute"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text("Start Time")
        }

        Button(onClick = {
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    endTime = "$hour:$minute"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text("End Time")
        }

        // CATEGORY DROPDOWN
        var expanded by remember { mutableStateOf(false) }

        Box {
            Button(onClick = { expanded = true }) {
                Text(
                    text = categories.find { it.id == selectedCategoryId }?.name
                        ?: "Select Category"
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            selectedCategoryId = it.id
                            expanded = false
                        }
                    )
                }
            }
        }

        // PHOTO PICKER
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                photoUri = uri?.toString()
            }

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Add Photo (Optional)")
        }

        Button(onClick = {

            val amt = amount.toDoubleOrNull()

            if (description.isBlank() || amt == null || selectedCategoryId == null) {
                Toast.makeText(context, "Fill required fields", Toast.LENGTH_SHORT).show()
                return@Button
            }

            scope.launch(Dispatchers.IO) {
                dao.insertExpense(
                    Expense(
                        description = description,
                        amount = amt,
                        date = selectedDate,
                        startTime = startTime,
                        endTime = endTime,
                        categoryId = selectedCategoryId!!,
                        photoUri = photoUri
                    )
                )
            }

            Toast.makeText(context, "Expense Saved", Toast.LENGTH_SHORT).show()
        }) {
            Text("Save Expense")
        }
    }
}
