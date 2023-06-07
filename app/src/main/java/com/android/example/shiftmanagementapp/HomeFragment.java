package com.android.example.shiftmanagementapp;
import android.os.Bundle;
import android.os.Handler;
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

public class HomeFragment extends Fragment {
    
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
            // Perform any desired action when the button is activated
            // For example, enable another component or trigger an event
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
        }
    };
}
