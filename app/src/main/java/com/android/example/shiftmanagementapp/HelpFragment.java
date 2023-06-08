package com.android.example.shiftmanagementapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class HelpFragment extends Fragment {
    
    private LinearLayout container;
    private Button _removeButton;
    
    public HelpFragment() {
        // Required empty public constructor
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        
        this.container = view.findViewById(R.id.container);
    
        showDataList();
        
        return view;
    }
    
    
    private void addBlock(long start, long end) {
        View blockView = getLayoutInflater().inflate(R.layout.item_block, container, false);
        TextView textView = blockView.findViewById(R.id.textBlock);
        textView.setText("Start: " + DateUtils.formatDateTime(start) + "\n" +
                         "End:   " + DateUtils.formatDateTime(end));
        container.addView(blockView);
        
        Button remove = blockView.findViewById(R.id.removeBlock);
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Removed" + start, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showDataList()
    {
        container.removeAllViews();
        for (int i = 0; i < ShiftActivity.DataList.size() - 1; i += 2)
        {
            addBlock(ShiftActivity.DataList.get(i).getTimestamp(), ShiftActivity.DataList.get(i + 1).getTimestamp());
        }
    }
    
}