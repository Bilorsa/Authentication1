package com.example.authentication1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private TextInputEditText inputName, inputEmail, inputPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        inputName = findViewById(R.id.register_name);
        inputEmail = findViewById(R.id.register_email);
        inputPassword = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.btn_register_submit);

        btnRegister.setOnClickListener(v -> executeRegistration());
    }

    private void executeRegistration() {
        String name = String.valueOf(inputName.getText()).trim();
        String email = String.valueOf(inputEmail.getText()).trim();
        String password = String.valueOf(inputPassword.getText()).trim();

        if (TextUtils.isEmpty(email) || password.length() < 6) {
            Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Initialize user profile in the database
                    HashMap<String, String> profile = new HashMap<>();
                    profile.put("name", name);
                    profile.put("email", email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.getUid())
                            .setValue(profile)
                            .addOnSuccessListener(aVoid -> {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            });
                }
            } else {
                btnRegister.setEnabled(true);
                Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}