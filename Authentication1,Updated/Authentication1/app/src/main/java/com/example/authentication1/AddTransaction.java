package com.example.authentication1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddTransaction extends AppCompatActivity {
    private TextInputEditText editAmount, editCategory;
    private RadioButton radioExpense;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        editAmount = findViewById(R.id.editAmount);
        editCategory = findViewById(R.id.editCategory);
        radioExpense = findViewById(R.id.radioExpense);
        btnSave = findViewById(R.id.btn_save_transaction);

        btnSave.setOnClickListener(v -> pushTransactionData());
    }

    private void pushTransactionData() {
        String amountStr = String.valueOf(editAmount.getText()).trim();
        String category = String.valueOf(editCategory.getText()).trim();
        String type = radioExpense.isChecked() ? "Expense" : "Income";

        if (TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(category)) return;

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            editAmount.setError("Invalid number");
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Transactions").child(userId);

        String transId = UUID.randomUUID().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Transaction transaction = new Transaction(transId, type, amount, category, date);

        btnSave.setEnabled(false);
        dbRef.child(transId).setValue(transaction).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish(); // Returns to Dashboard
            } else {
                btnSave.setEnabled(true);
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class Transaction {
        public String id, type, category, date;
        public double amount;

        public Transaction() {
            // Default constructor required for calls to DataSnapshot.getValue(Transaction.class)
        }

        public Transaction(String id, String type, double amount, String category, String date) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }
    }
}