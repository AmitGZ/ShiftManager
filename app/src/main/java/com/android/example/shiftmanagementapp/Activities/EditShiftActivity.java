package com.android.example.shiftmanagementapp.Activities;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.example.shiftmanagementapp.R;
import com.android.example.shiftmanagementapp.Utils.ShiftData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class EditShiftActivity extends ModifyShiftActivity {
    
    private String _shiftId;
    private Button _deleteButton;
    private EditText _hourlyRateInput;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shift);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        // Initialize HourlyRate
        _hourlyRate = getIntent().getDoubleExtra("hourlyRate",30);
        _shiftId =    getIntent().getStringExtra("shiftId");
        startDateTime.setTimeInMillis(getIntent().getLongExtra  ("shiftStart",0));
        endDateTime.  setTimeInMillis(getIntent().getLongExtra  ("shiftEnd",0));
        
        // Initialize views
        startTimeEditText = findViewById(R.id.startTimeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        submitButton = findViewById(R.id.submitButton);
        errorText = findViewById(R.id.errorLabel);
        _deleteButton = findViewById(R.id.deleteButton);
    
        _hourlyRateInput = findViewById(R.id.hourlyRate);
        _hourlyRateInput.setFilters(new InputFilter[] { new InputFilter.AllCaps(), new InputFilter.LengthFilter(10) });
        _hourlyRateInput.setText(String.valueOf(_hourlyRate));
    
        setStartAndEndEdit();
        
        updateDateTimeTextDate(startDateEditText, startDateTime);
        updateDateTimeTextTime(startTimeEditText, startDateTime);
        updateDateTimeTextDate(endDateEditText, endDateTime);
        updateDateTimeTextTime(endTimeEditText, endDateTime);
        
        setSubmitButton();
        
        setDeleteButton();
    }
    
    private void setSubmitButton()
    {
        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiftActivity._userDatabaseRef.child("logs").child(_shiftId)
                        .setValue(new ShiftData(startDateTime.getTimeInMillis(), endDateTime.getTimeInMillis(), Double.parseDouble(_hourlyRateInput.getText().toString())));
                setResult(EditShiftActivity.RESULT_OK);
                finish();
            }
        });
    }
    
    private void deleteShift()
    {
        ShiftActivity._userDatabaseRef.child("logs").child(_shiftId).removeValue(new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
            {
                if (databaseError != null)
                {
                    System.out.println("Data could not be removed. " + databaseError.getMessage());
                }
                else
                {
                    System.out.println("Data removed successfully.");
                    onBackPressed();
                }
            }
        });
    }
    
    private void setDeleteButton()
    {
        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteShift();
                setResult(EditShiftActivity.RESULT_OK);
                finish();
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