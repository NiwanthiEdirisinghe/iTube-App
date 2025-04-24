package com.example.itubeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {
    private EditText fullNameEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button createAccountButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        fullNameEditText = findViewById(R.id.full_name_edit_text);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.getUserByUsername(username).getCount() > 0) {
                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                long result = dbHelper.insertUser(username, password, fullName);

                if (result != -1) {
                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to login screen
                } else {
                    Toast.makeText(SignUpActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}