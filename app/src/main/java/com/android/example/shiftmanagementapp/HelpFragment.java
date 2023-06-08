package com.android.example.shiftmanagementapp;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class HelpFragment extends Fragment {
    
    private LinearLayout _container;
    private Button _removeButton;
    
    private final FirebaseUser _user;
    private final DatabaseReference _databaseRef;
    
    public HelpFragment (@NotNull FirebaseUser user, @NotNull DatabaseReference databaseRef)
    {
        _user = user;
        _databaseRef = databaseRef;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, _container, false);
        
        _container = view.findViewById(R.id.container);
    
        showDataList();
        
        return view;
    }
    
    
    private void addBlock(long start, long end)
    {
        View blockView = getLayoutInflater().inflate(R.layout.item_block, _container, false);
        TextView textView = blockView.findViewById(R.id.textBlock);
        textView.setText("Start: " + DateUtils.formatDateTime(start) + "\n" + "End:   " + DateUtils.formatDateTime(end));
        _container.addView(blockView);
    
        Button remove = blockView.findViewById(R.id.removeBlock);
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Removed" + start, Toast.LENGTH_SHORT).show();
                _container.removeView(blockView);
    
                Query queryStart = _databaseRef.orderByChild("timestamp").equalTo(start);
                Query queryEnd = _databaseRef.orderByChild("timestamp").equalTo(end);
    
                // Attach a ValueEventListener to the query for a single event
                queryStart.addListenerForSingleValueEvent(new RemoveEventListener(getActivity()));
                queryEnd.addListenerForSingleValueEvent(new RemoveEventListener(getActivity()));
            }
        });
    }
    private void showDataList()
    {
        _container.removeAllViews();
        for (int i = 0; i < ShiftActivity.DataList.size() - 1; i += 2)
            addBlock(ShiftActivity.DataList.get(i).getTimestamp(), ShiftActivity.DataList.get(i + 1).getTimestamp());
    }
    
}

// Remove event listener from database
class RemoveEventListener implements ValueEventListener
{
    Activity _activity;
    
    RemoveEventListener(Activity activity)
    {
        _activity = activity;
    }
    
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
    {
        // Iterate through the matching items and delete them
        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
        {
            itemSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    // Item removal successful
                    Toast.makeText(_activity, "Removed from database successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    // Item removal failed
                    Toast.makeText(_activity, "Failed to removed from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    @Override
    public void onCancelled(@NonNull DatabaseError error)
    {
        Toast.makeText(_activity, "Failed to removed from database: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}