package com.android.example.shiftmanagementapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private TextView _salaryText;
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
        _salaryText = view.findViewById(R.id.salary);
    
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
                editShiftIntent.putExtra("hourlyRate",(double) ShiftActivity.HourlyRate);
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
        _salaryText.setText("Salary: " + String.format("%.2f", totalSalary) + " $");
    }
    
}