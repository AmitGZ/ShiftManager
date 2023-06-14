package com.android.example.shiftmanagementapp;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {
    
    private EditText _hourlyRateInput;
    
    private final FirebaseUser _user;
    private final DatabaseReference _userDatabaseRef;
    
    public SettingsFragment(@NotNull FirebaseUser user, @NotNull DatabaseReference databaseRef)
    {
        _user = user;
        _userDatabaseRef = databaseRef;
    
        // Set Hourly Rate
        _userDatabaseRef.child("hourlyRate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The "HourlyRate" child exists in the database
                    ShiftActivity.HourlyRate = dataSnapshot.getValue(Double.class);
                } else {
                    // The "HourlyRate" child does not exist in the database
                    _userDatabaseRef.child("hourlyRate").setValue(ShiftActivity.HourlyRate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the listener is canceled
            }
        });
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
    
        _hourlyRateInput = view.findViewById(R.id.hourlyRate);
        _hourlyRateInput.setFilters(new InputFilter[] { new InputFilter.AllCaps(), new InputFilter.LengthFilter(10) });
        _hourlyRateInput.setText(String.valueOf(ShiftActivity.HourlyRate));
    
        _hourlyRateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Handle the input change here
                String input = _hourlyRateInput.getText().toString();
            
                // Validate the input
                if (!input.isEmpty()) {
                    try {
                        ShiftActivity.HourlyRate = Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), "Not a number!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        
            @Override
            public void afterTextChanged(Editable editable) {
                _userDatabaseRef.child("hourlyRate").setValue(ShiftActivity.HourlyRate);
            }
        });
        
        return view;
    }
}