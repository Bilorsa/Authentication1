package com.example.authentication1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textTotalBalance, textIncome, textExpense;
    private DatabaseReference transactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, Login1.class));
            finish();
            return;
        }

        textTotalBalance = findViewById(R.id.balance_amount);
        textIncome = findViewById(R.id.tv_income_total);
        textExpense = findViewById(R.id.tv_expense_total);

        // Map navigation
        findViewById(R.id.logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login1.class));
            finish();
        });

        findViewById(R.id.fab_add_transaction).setOnClickListener(v ->
                startActivity(new Intent(this, AddTransaction.class))
        );

        // Initialize Realtime Data Stream
        transactionRef = FirebaseDatabase.getInstance().getReference("Transactions").child(currentUser.getUid());
        attachDatabaseListener();
    }

    private void attachDatabaseListener() {
        transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalIncome = 0;
                double totalExpense = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    Transaction t = data.getValue(Transaction.class);
                    if (t != null) {
                        if ("Income".equals(t.getType())) totalIncome += t.getAmount();
                        else if ("Expense".equals(t.getType())) totalExpense += t.getAmount();
                    }
                }

                double netBalance = totalIncome - totalExpense;

                // Format UI outputs
                textTotalBalance.setText(String.format(Locale.getDefault(), "$%.2f", netBalance));
                textIncome.setText(String.format(Locale.getDefault(), "+$%.2f", totalIncome));
                textExpense.setText(String.format(Locale.getDefault(), "-$%.2f", totalExpense));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class Transaction {
    private String type;
    private double amount;

    public Transaction() {
        // Required for Firebase
    }

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
