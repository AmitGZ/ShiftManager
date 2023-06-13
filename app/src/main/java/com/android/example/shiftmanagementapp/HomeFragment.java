package com.android.example.shiftmanagementapp;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    
    private static final int DELAY_MILLIS = 1000;
    
    private ImageButton activationButton;
    private Handler _buttonHandler;
    private Handler _timeHandler;
    private Animation scaleAnimation;
    private TextView _startStopText;
    private TextView _timeInShift;
    private long _lastPressed;
    private boolean _isInShift;
    
    private final FirebaseUser _user;
    private final DatabaseReference _userDatabaseRef;
    
    public HomeFragment(@NotNull FirebaseUser user, @NotNull DatabaseReference databaseRef)
    {
        _user = user;
        _userDatabaseRef = databaseRef;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
    
        _startStopText = view.findViewById(R.id.startStopText);
        _timeInShift = view.findViewById(R.id.timeInShift);
        
        setActivationButton(view);
    
        setTimeHandler();
        
        // Inflate the layout for this fragment
        return view;
    }
    
    private Runnable activationRunnable = new Runnable() {
        @Override
        public void run()
        {
            // Create a new data object with relevant information
            UserData userData = new UserData(_user.getUid(), System.currentTimeMillis());
            
            // Save the data to the Firebase Realtime Database
            _userDatabaseRef.child("logs").push().setValue(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data saved successfully
                            Toast.makeText(getActivity(), "Data saved!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while saving data
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Data save failed", e);
                        }
                    });
        }
    };
    
    private void setActivationButton(View view)
    {
        // Setting button
        activationButton = view.findViewById(R.id.imageButton);
        _buttonHandler = new Handler();
        scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_animation);
    
        activationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start the delayed activation check
                        _buttonHandler.postDelayed(activationRunnable, DELAY_MILLIS);
                        v.startAnimation(scaleAnimation);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Cancel the delayed activation check
                        _buttonHandler.removeCallbacks(activationRunnable);
                        v.clearAnimation();
                        return true;
                }
                return false;
            }
        });
    }
    
    private void setTimeHandler()
    {
        _timeHandler = new Handler();
    
        // Create a new Runnable to update the TextView
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                _isInShift = (ShiftActivity.DataList.size() % 2 == 1);
                _lastPressed = ShiftActivity.DataList.size() > 0 ? ShiftActivity.DataList.get(ShiftActivity.DataList.size() - 1).getTimestamp() : 0;
    
                // Format the time elapsed as desired (e.g., minutes:seconds)
                String formattedTime = DateUtils.getTimeDifference(_lastPressed, System.currentTimeMillis());
            
                // Update the TextView with the formatted time
                _timeInShift.setText(_isInShift ? formattedTime : "");
    
                _startStopText.setText(_isInShift ? "Hold to end shift" : "Hold to start shift");
    
                // Schedule the next update
                _timeHandler.postDelayed(this, 10);
            }
        };
    
        // Start the initial update
        _timeHandler.post(updateRunnable);
    }
}
