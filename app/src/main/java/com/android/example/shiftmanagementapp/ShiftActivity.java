package com.android.example.shiftmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShiftActivity extends AppCompatActivity
{
    private FirebaseAuth _firebaseAuth;
    private FirebaseUser _user;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        _firebaseAuth = FirebaseAuth.getInstance();
        _user = _firebaseAuth.getCurrentUser();
        if (_user != null)
        {
            TextView helloText = findViewById(R.id.helloText);
            helloText.setText("Hello " + _user.getDisplayName());
        }
        
        ImageButton myImageButton = findViewById(R.id.imageButton);
        myImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("Click");
            }
        });
    
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.home_item:
                    // Handle click for home item
                    return true;
                case R.id.search_item:
                    // Handle click for search item
                    return true;
                case R.id.profile_item:
                    // Handle click for profile item
                    return true;
                default:
                    return false;
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                _firebaseAuth.signOut();
                if (_user != null)
                    Toast.makeText(ShiftActivity.this, "Signed out: " + _user.getDisplayName(), Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}