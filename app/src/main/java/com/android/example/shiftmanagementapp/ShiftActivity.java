package com.android.example.shiftmanagementapp;

import android.os.Bundle;
import android.view.MenuItem;
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
    private DatabaseReference _userDatabaseRef;
    
    private HomeFragment _homeFragment;
    private ListFragment _listFragment;
    private SettingsFragment _settingsFragment;
    
    static public double HourlyRate;
    static public List<ShiftData> DataList;
    static public long LastPressed;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    
        DataList = new ArrayList<ShiftData>();
        HourlyRate = 30.0;
        LastPressed = 0;
        
        // Home as up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Setting User database
        _firebaseAuth = FirebaseAuth.getInstance();
        _user = _firebaseAuth.getCurrentUser();
        _userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(_user.getUid());;
        if (_user == null || _firebaseAuth == null || _userDatabaseRef == null)
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
        setLogsQuery();
        
        setShiftQuery();
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
        _homeFragment = new HomeFragment(_user, _userDatabaseRef);
        _listFragment = new ListFragment(_user, _userDatabaseRef);
        _settingsFragment = new SettingsFragment(_user, _userDatabaseRef);
    
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_item);
        openFragment(_homeFragment);
    
        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.home_item:
                    openFragment(_homeFragment);
                    return true;
                case R.id.list_item:
                    openFragment(_listFragment);
                    return true;
                case R.id.settings_item:
                    openFragment(_settingsFragment);
                    return true;
                default:
                    return false;
            }
        });
    }
    
    private void setLogsQuery()
    {
        Query query = _userDatabaseRef.child("logs").orderByChild("timestamp");
    
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // Handle the retrieved data
                DataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShiftData data = snapshot.getValue(ShiftData.class);
                    data.setKey(snapshot.getKey());
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
    
    private void setShiftQuery()
    {
        _userDatabaseRef.child("LastPressed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The "HourlyRate" child exists in the database
                    ShiftActivity.LastPressed = dataSnapshot.getValue(Long.class);
                } else {
                    // The "HourlyRate" child does not exist in the database
                    _userDatabaseRef.child("LastPressed").setValue((long)0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the listener is canceled
            }
        });
    }
    
}