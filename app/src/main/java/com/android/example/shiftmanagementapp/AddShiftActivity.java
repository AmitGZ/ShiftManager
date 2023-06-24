package com.android.example.shiftmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        startTimeEditText = findViewById(R.id.startTimeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        submitButton = findViewById(R.id.submitButton);

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
                System.out.println("Start: " + startDateTime.getTimeInMillis());
                System.out.println("end: " + endDateTime.getTimeInMillis());
                if (isValidDateTime(endDateTime,startDateTime)) {
                    // Perform your submit logic here
                    Toast.makeText(AddShiftActivity.this, "Good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddShiftActivity.this, "Bad", Toast.LENGTH_SHORT).show();
                }
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