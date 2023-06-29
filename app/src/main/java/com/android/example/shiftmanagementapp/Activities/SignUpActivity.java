package com.android.example.shiftmanagementapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.example.shiftmanagementapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class SignUpActivity extends AppCompatActivity {
    
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        ActionBar actionBar = getSupportActionBar();
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Initialize the views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
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
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        
        // Check if the username, email, and password are valid
        if (isValidUsername(username) && isValidEmail(email) && isValidPassword(password)) {
            // Create a new user with the specified email and password
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign-up success
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // Set the username as a custom claim
                            setUserUsername(username);
                            Toast.makeText(SignUpActivity.this, "Sign-up successful! User ID: " + user.getUid(), Toast.LENGTH_SHORT).show();
                            
                            onBackPressed();
                        } else {
                            // Sign-up failed
                            Exception exception = task.getException();
                            Toast.makeText(SignUpActivity.this, "Sign-up failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            
        } else {
            // Show an error message
            Toast.makeText(this, "Invalid username, email, or password.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean isValidUsername(String username) {
        return username.length() >= 3;
    }
    
    private boolean isValidEmail(String email) {
        // You can add your own email validation logic here if needed
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
    
    private void setUserUsername(String username) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Username set successfully
                            Toast.makeText(SignUpActivity.this, "Username set successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to set username
                            Exception exception = task.getException();
                            Toast.makeText(SignUpActivity.this, "Failed to set username: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
