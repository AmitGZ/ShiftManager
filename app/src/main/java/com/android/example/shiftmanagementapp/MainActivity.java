package com.android.example.shiftmanagementapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        FirebaseApp.initializeApp(this);
    
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
    
    
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    
    
        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sign Up Screen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void login()
    {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
    
        // Sign in the user with the specified email and password
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Login successful! User ID: " + user.getUid(), Toast.LENGTH_SHORT).show();
                    
                        // You can navigate to another activity upon successful login
                        Intent intent = new Intent(MainActivity.this, ShiftActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed
                        Exception exception = task.getException();
                        Toast.makeText(MainActivity.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
