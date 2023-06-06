package com.android.example.shiftmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShiftActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
            TextView helloText = findViewById(R.id.helloText);
            helloText.setText("Hello " + user.getDisplayName());
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
                firebaseAuth.signOut();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}