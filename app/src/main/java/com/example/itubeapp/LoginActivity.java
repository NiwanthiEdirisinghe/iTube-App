package com.example.itubeapp;

import android.content.Intent;
import android.database.Cursor;
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

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                authenticateUser(username, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void authenticateUser(String username, String password) {
        Cursor cursor = dbHelper.getUserByUsername(username);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");
            int userIdColumnIndex = cursor.getColumnIndex("id");

            if (passwordColumnIndex != -1 && userIdColumnIndex != -1) {
                String storedPassword = cursor.getString(passwordColumnIndex);
                long userId = cursor.getLong(userIdColumnIndex);

                if (password.equals(storedPassword)) {
                    sessionManager.createLoginSession(userId, username);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}