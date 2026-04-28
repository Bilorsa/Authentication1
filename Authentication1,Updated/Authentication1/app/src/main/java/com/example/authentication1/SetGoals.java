package com.example.authentication1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetGoals extends AppCompatActivity {

    private TextInputEditText editMinGoal, editMaxGoal;
    private Button btnSaveGoals;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        // Bind UI Components
        editMinGoal = findViewById(R.id.editMinGoal);
        editMaxGoal = findViewById(R.id.editMaxGoal);
        btnSaveGoals = findViewById(R.id.btn_save_goals);

        // Verify Session
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Point reference to a specific "Goals" node under the user's ID
            databaseReference = FirebaseDatabase.getInstance().getReference("Goals").child(currentUser.getUid());
            loadExistingGoals();
        } else {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSaveGoals.setOnClickListener(v -> executeGoalUpdate());
    }

    private void executeGoalUpdate() {
        String minStr = String.valueOf(editMinGoal.getText()).trim();
        String maxStr = String.valueOf(editMaxGoal.getText()).trim();

        // 1. Check for empty fields
        if (TextUtils.isEmpty(minStr)) {
            editMinGoal.setError("Minimum goal is required");
            return;
        }
        if (TextUtils.isEmpty(maxStr)) {
            editMaxGoal.setError("Maximum goal is required");
            return;
        }

        try {
            double minGoal = Double.parseDouble(minStr);
            double maxGoal = Double.parseDouble(maxStr);

            // 2. Logical Validation
            if (minGoal >= maxGoal) {
                editMaxGoal.setError("Maximum limit must be strictly greater than the minimum limit.");
                return;
            }

            // 3. UI State Management (Prevent multiple rapid submissions)
            btnSaveGoals.setEnabled(false);
            btnSaveGoals.setText("Saving...");

            // 4. Push to Realtime Database
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            BudgetGoal updatedGoals = new BudgetGoal(minGoal, maxGoal, currentDate);

            databaseReference.setValue(updatedGoals).addOnCompleteListener(task -> {
                // Restore button state
                btnSaveGoals.setEnabled(true);
                btnSaveGoals.setText("Update Goals");

                if (task.isSuccessful()) {
                    Toast.makeText(SetGoals.this, "Monthly goals successfully synced.", Toast.LENGTH_SHORT).show();
                    finish(); // Navigate back down the backstack
                } else {
                    Toast.makeText(SetGoals.this, "Network error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Critical error parsing numerical inputs.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExistingGoals() {
        // Fetch snapshot to populate fields for a better User Experience
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                BudgetGoal currentGoal = dataSnapshot.getValue(BudgetGoal.class);
                if (currentGoal != null) {
                    editMinGoal.setText(String.valueOf(currentGoal.getMinGoal()));
                    editMaxGoal.setText(String.valueOf(currentGoal.getMaxGoal()));
                }
            }
        });
    }
}

