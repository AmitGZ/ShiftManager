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
import java.util.TimeZone;

public class ModifyShiftActivity extends AppCompatActivity {
    
    protected Calendar startDateTime;
    protected Calendar endDateTime;
    
    // Initialize views
    protected EditText startTimeEditText;
    protected EditText startDateEditText;
    protected EditText endTimeEditText;
    protected EditText endDateEditText;
    
    protected Button submitButton;
    protected TextView errorText;
    
    protected double _hourlyRate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        startDateTime = Calendar.getInstance();
        endDateTime = Calendar.getInstance();
        startDateTime.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        endDateTime.setTimeZone(TimeZone.getTimeZone("GMT+2"));
    }
    
    protected void setStartAndEndEdit()
    {
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
    }
    
    protected void addShift()
    {
        if(!isDateTimeEmpty())
        {
            if (isValidDateTime(endDateTime,startDateTime))
            {
                ShiftActivity._userDatabaseRef.child("logs").push().setValue(new ShiftData(startDateTime.getTimeInMillis(), endDateTime.getTimeInMillis(), _hourlyRate));
                onBackPressed();
            }
            else
            {
                errorText.setText("Shift can not be ended before started");
            }
        }
        else
        {
            errorText.setText("You must fill all the fields to add shift");
        }
    }
    
    private void showDatePicker(final EditText editText, @NonNull final Calendar dateTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(ModifyShiftActivity.this,
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(ModifyShiftActivity.this,
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
                DateFormat.is24HourFormat(ModifyShiftActivity.this));
        timePickerDialog.show();
    }
    
    protected void updateDateTimeTextDate(EditText editText, Calendar dateTime) {
        String formattedDate = DateFormat.format("dd/MM/yyyy", dateTime).toString();
        editText.setText(formattedDate);
    }
    
    protected void updateDateTimeTextTime(EditText editText, Calendar dateTime) {
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