package com.android.example.shiftmanagementapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class ListFragment extends Fragment {
    
    private LinearLayout _container;
    private Button _salaryButton;
    private static final int CHILD_ACTIVITY_REQUEST_CODE = 1;
    
    private final FirebaseUser _user;
    private final DatabaseReference _userDatabaseRef;
    
    public ListFragment (@NotNull FirebaseUser user, @NotNull DatabaseReference databaseRef)
    {
        _user = user;
        _userDatabaseRef = databaseRef;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, _container, false);
        
        _container = view.findViewById(R.id.container);
        _salaryButton = view.findViewById(R.id.salary);
        _salaryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupWindow();
            }
        });
    
        Drawable vectorDrawable = getResources().getDrawable(R.drawable.ic_baseline_sms_24);
        vectorDrawable.setBounds(0, 0, 100, 100); // Set the desired size of the drawable
        _salaryButton.setCompoundDrawables(vectorDrawable, null, null, null); // Set the drawable at the start (left)
    
        refreshDataList();
        
        return view;
    }
    
    private void addBlock(ShiftData shiftData)
    {
        View blockView = getLayoutInflater().inflate(R.layout.item_block, _container, false);
        TextView textView = blockView.findViewById(R.id.textBlock);
        textView.setText("Start: " + DateUtils.formatDateTime(shiftData.getStart()) + "\n" + "End:   " + DateUtils.formatDateTime(shiftData.getEnd()));
        _container.addView(blockView);
    
        Button removeButton = blockView.findViewById(R.id.removeBlock);
        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editShiftIntent = new Intent(getActivity(), EditShiftActivity.class);
                editShiftIntent.putExtra("hourlyRate",(double) shiftData.getHourlyRate());
                editShiftIntent.putExtra("shiftId",   (String) shiftData.getKey());
                editShiftIntent.putExtra("shiftStart",(long)   shiftData.getStart());
                editShiftIntent.putExtra("shiftEnd",  (long)   shiftData.getEnd());
                startActivityForResult(editShiftIntent, CHILD_ACTIVITY_REQUEST_CODE);
                
                refreshDataList();
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == CHILD_ACTIVITY_REQUEST_CODE) {
            refreshDataList();
        }
    }
    
    
    public void refreshDataList()
    {
        double totalSalary = 0.0D;
        _container.removeAllViews();
        for (int i = 0; i < ShiftActivity.DataList.size(); i++)
        {
            addBlock(ShiftActivity.DataList.get(i));
            totalSalary += ShiftActivity.DataList.get(i).getShiftSalary();
        }
        _salaryButton.setText("Salary: " + String.format("%.2f", totalSalary) + " $");
    }
    
    private void showPopupWindow() {
        // Inflate the popup layout
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);
        
        // Create the popup window
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        
        // Set a background drawable for the popup window
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
        // Set focusable to true to allow interaction with the popup window
        popupWindow.setFocusable(true);
        
        // Find the root view of the activity or fragment
        final View rootView = getActivity().getWindow().getDecorView().getRootView();
        
        // Apply a greyed-out overlay to the background behind the popup
        final int overlayColor = getResources().getColor(R.color.purple_500); // Define the desired overlay color
        final int overlayAlpha = 150; // Define the desired overlay transparency (0-255)
        rootView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                Drawable overlay = new ColorDrawable(ColorUtils.setAlphaComponent(overlayColor, overlayAlpha));
                rootView.setForeground(overlay);
            }
        });
        
        // Set an OnDismissListener to remove the overlay when the popup window is dismissed
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDismiss() {
                rootView.setForeground(null);
            }
        });
        
        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        
        // Get references to the Yes and No buttons
        Button yesButton = popupView.findViewById(R.id.yesButton);
        Button noButton = popupView.findViewById(R.id.noButton);
        
        // Set click listeners for the Yes and No buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Handle Yes button click
                // ...
                // Dismiss the popup window and remove the overlay
                popupWindow.dismiss();
                rootView.setForeground(null);
            }
        });
        
        noButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Handle No button click
                // ...
                // Dismiss the popup window and remove the overlay
                popupWindow.dismiss();
                rootView.setForeground(null);
            }
        });
    }


    
    
}