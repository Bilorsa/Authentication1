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
import com.example.budgettracker.data.local.entities.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryActivity : ComponentActivity() {

    private lateinit var dao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = AppDatabase.getDatabase(this).budgetDao()

        setContent {
            CategoryScreen(dao)
        }
    }
}

@Composable
fun CategoryScreen(dao: BudgetDao) {

    var categoryName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            if (categoryName.isBlank()) {
                Toast.makeText(context, "Enter category", Toast.LENGTH_SHORT).show()
                return@Button
            }

            CoroutineScope(Dispatchers.IO).launch {
                dao.insertCategory(Category(name = categoryName))
            }

            Toast.makeText(context, "Category Added", Toast.LENGTH_SHORT).show()
            categoryName = ""

        }) {
            Text("Save Category")
        }
    }
}
