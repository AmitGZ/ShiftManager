package com.android.example.shiftmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShiftActivity extends AppCompatActivity
{
    private FirebaseAuth _firebaseAuth;
    private FirebaseUser _user;
    private DatabaseReference _databaseRef;
    
    private HomeFragment _homeFragment;
    private HelpFragment _helpFragment;
    private SettingsFragment _settingsFragment;
    
    static public List<UserData> DataList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    
        DataList = new ArrayList<UserData>();
        
        // Home as up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Setting User database
        _firebaseAuth = FirebaseAuth.getInstance();
        _user = _firebaseAuth.getCurrentUser();
        _databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(_user.getUid());;
        if (_user == null || _firebaseAuth == null || _databaseRef == null)
        {
            Toast.makeText(this, "User authentication failed", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity
            return;
        }
        
        // Set Hello User text
        TextView helloText = findViewById(R.id.helloText);
        helloText.setText("Hello " + _user.getDisplayName());
    
        // Setting bottom navigation bar
        setBottomNavigation();
    
        // Setting database on data change
        setDatabaseQuery();
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
    
    private void setBottomNavigation()
    {
        // Setting up bottom navigation
        _homeFragment = new HomeFragment(_user, _databaseRef);
        _helpFragment = new HelpFragment(_user, _databaseRef);
        _settingsFragment = new SettingsFragment();
    
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_item);
        openFragment(_homeFragment);
    
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
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
    
    private void setDatabaseQuery()
    {
        Query query = _databaseRef.orderByChild("userId");
    
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // Handle the retrieved data
                DataList.clear();
                long lastTimeStamp = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserData data = snapshot.getValue(UserData.class);
                    DataList.add(data);
                }
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
                Toast.makeText(ShiftActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}