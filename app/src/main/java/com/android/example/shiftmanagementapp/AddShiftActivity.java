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

public class AddShiftActivity extends AppCompatActivity {

    private Calendar startDateTime;
    private Calendar endDateTime;

    // Initialize views
    private EditText startTimeEditText;
    private EditText startDateEditText;
    private EditText endTimeEditText;
    private EditText endDateEditText;
    private Button submitButton;

    private TextView errorText;

    private DatabaseReference _userDatabaseRef;

    private double _hourlyRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize DatabaseReference
        String databaseRefString = getIntent().getStringExtra("userDatabaseRef");
        System.out.println("DatabaseRef : " + databaseRefString);

        // TODO - FIX: Raising an exception.
        _userDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(databaseRefString);

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

        // Initialize Calendar instances
        startDateTime = Calendar.getInstance();
        endDateTime = Calendar.getInstance();

        // Set click listeners for start date and time pickers
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(startDateEditText, startDateTime);
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(startTimeEditText, startDateTime);
            }
        });

        // Set click listeners for end date and time pickers
        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(endDateEditText, endDateTime);
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(endTimeEditText, endDateTime);
            }
        });

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDateTimeEmpty())
                {
                    errorText.setText("You must fill all the fields to add shift");
                    return;
                }
                System.out.println("Start: " + startDateTime.getTimeInMillis());
                System.out.println("end: " + endDateTime.getTimeInMillis());
                if (!isValidDateTime(endDateTime,startDateTime)) {
                    errorText.setText("Shift can not be ended before started");
                    return;
                }
                // TODO -  Insert shift to database
                errorText.setText("");
/*
                // Create a new data object with relevant information
                ShiftData shiftData = new ShiftData(startDateTime.getTimeInMillis(), endDateTime.getTimeInMillis(), ShiftActivity.HourlyRate);

                // Save the data to the Firebase Realtime Database
                _userDatabaseRef.child("logs").push().setValue(shiftData)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                // Data saved successfully
                                Toast.makeText(AddShiftActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();
                                //TODO - Go back to HomeFragment
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                // Error occurred while saving data
                                Toast.makeText(AddShiftActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Data save failed", e);
                            }
                        });
                */
            }
        });
    }

    private void showDatePicker(final EditText editText, final Calendar dateTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddShiftActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Clear the time components
                        dateTime.clear(Calendar.HOUR_OF_DAY);
                        dateTime.clear(Calendar.MINUTE);
                        dateTime.clear(Calendar.SECOND);

                        // Set the selected date
                        dateTime.set(Calendar.YEAR, year);
                        dateTime.set(Calendar.MONTH, month);
                        dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateDateTimeTextDate(editText, dateTime);
                    }
                },
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void showTimePicker(final EditText editText, final Calendar dateTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddShiftActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        dateTime.set(Calendar.MINUTE, minute);
                        updateDateTimeTextTime(editText, dateTime);
                    }
                },
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(AddShiftActivity.this));
        timePickerDialog.show();
    }

    private void updateDateTimeTextDate(EditText editText, Calendar dateTime) {
        String formattedDate = DateFormat.format("dd/MM/yyyy", dateTime).toString();
        editText.setText(formattedDate);
    }

    private void updateDateTimeTextTime(EditText editText, Calendar dateTime) {
        String formattedTime = DateFormat.format("HH:mm", dateTime).toString();
        editText.setText(formattedTime);
    }

    private boolean isValidDateTime(Calendar endDateTime,Calendar startDateTime) {
        return endDateTime.after(startDateTime);
    }

    private boolean isDateTimeEmpty() {
        if(startDateEditText.getText().toString().isEmpty())
            return true;
        if(startTimeEditText.getText().toString().isEmpty())
            return  true;
        if(endDateEditText.getText().toString().isEmpty())
            return true;
        if(endTimeEditText.getText().toString().isEmpty())
            return true;
        return false;
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