package com.android.example.shiftmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddShiftActivity extends ModifyShiftActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Initialize HourlyRate
        _hourlyRate = getIntent().getDoubleExtra("hourlyRate",30);
        System.out.println("Hourly : " + _hourlyRate);
    
        // Initialize views
        startTimeEditText = findViewById(R.id.startTimeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        submitButton = findViewById(R.id.submitButton);
        errorText = findViewById(R.id.errorLabel);
    
        setStartAndEndEdit();
    
        setSubmitButton();
    }
    
    private void setSubmitButton()
    {
        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShift();
            }
        });
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