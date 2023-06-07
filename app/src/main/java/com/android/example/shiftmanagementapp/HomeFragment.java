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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    
    DatabaseReference _databaseRef;
    private ImageButton activationButton;
    private Handler handler;
    private boolean buttonActivated;
    private Animation scaleAnimation;
    private static final int DELAY_MILLIS = 1000;
    
    
    public HomeFragment() {
        // Required empty public constructor
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
    
        activationButton = view.findViewById(R.id.imageButton);
        handler = new Handler();
    
        scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_animation);
        
        activationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start the delayed activation check
                        handler.postDelayed(activationRunnable, DELAY_MILLIS);
                        v.startAnimation(scaleAnimation);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Cancel the delayed activation check
                        handler.removeCallbacks(activationRunnable);
                        v.clearAnimation();
                        return true;
                }
                return false;
            }
        });
        
        // Inflate the layout for this fragment
        return view;
    }
    
    private Runnable activationRunnable = new Runnable() {
        @Override
        public void run() {
            // Activate the button
            buttonActivated = true;
            
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
    
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Get the current timestamp
            long timestamp = System.currentTimeMillis();
    
            // Create a new data object with relevant information
            UserData userData = new UserData(userId, timestamp);
    
            _databaseRef = FirebaseDatabase.getInstance().getReference();
    
            // Save the data to the Firebase Realtime Database
            _databaseRef.push().setValue(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data saved successfully
                            System.out.println("\n\n\nSUCCESS\n\n\n\n");
                            Toast.makeText(getActivity(), "Data saved!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while saving data
                            System.out.println("\n\n\nFALIURE\n\n\n\n");
                            System.out.println("FAIL");
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Data save failed", e);
                        }
                    });
        }
    };
}
