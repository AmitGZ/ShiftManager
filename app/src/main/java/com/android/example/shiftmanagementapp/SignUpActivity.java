package com.android.example.shiftmanagementapp;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;


public class SignUpActivity extends AppCompatActivity {
    
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Initialize the views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        
        // Set a click listener for the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }
    
    private void signUp() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if the username and password are valid
        if (isValidUsername(username) && isValidPassword(password))
        {
            // Create a new user with the specified email and password
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign-up success
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this, "Sign-up successful! User ID: " + user.getUid(), Toast.LENGTH_SHORT).show();
                            
                            // You can also navigate to another activity here if needed
                        } else {
                            // Sign-up failed
                            Exception exception = task.getException();
                            Toast.makeText(SignUpActivity.this, "Sign-up failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            
        } else {
            // Show an error message
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean isValidUsername(String username) {
        return username.length() >= 3;
    }
    
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
