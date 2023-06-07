package com.android.example.shiftmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShiftActivity extends AppCompatActivity
{
    private FirebaseAuth _firebaseAuth;
    private FirebaseUser _user;
    
    private HomeFragment _homeFragment;
    private HelpFragment _helpFragment;
    private SettingsFragment _settingsFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    
        // Home as up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Setting Hello User text
        _firebaseAuth = FirebaseAuth.getInstance();
        _user = _firebaseAuth.getCurrentUser();
        if (_user != null)
        {
            TextView helloText = findViewById(R.id.helloText);
            helloText.setText("Hello " + _user.getDisplayName());
        }
    
        // Setting up bottom navigation
        _homeFragment = new HomeFragment();
        _helpFragment = new HelpFragment();
        _settingsFragment = new SettingsFragment();
    
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_item);
        openFragment(_homeFragment);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            int x = item.getItemId();
            switch (item.getItemId()) {
                case R.id.home_item:
                    openFragment(_homeFragment);
                    return true;
                case R.id.help_item:
                    openFragment(_helpFragment);
                    return true;
                case R.id.settings_item:
                    openFragment(_settingsFragment);
                    return true;
                default:
                    return false;
            }
        });
    }
    
    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
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