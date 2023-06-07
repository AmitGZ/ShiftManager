package com.android.example.shiftmanagementapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HelpFragment extends Fragment {
    
    private LinearLayout container;
    
    
    public HelpFragment() {
        // Required empty public constructor
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        
        this.container = view.findViewById(R.id.container);
        
        for (int i =0; i<100; i++)
            addBlock("Block " + i);
        
        return view;
    }
    
    
    private void addBlock(String text) {
        View blockView = getLayoutInflater().inflate(R.layout.item_block, container, false);
        TextView textView = blockView.findViewById(R.id.textBlock);
        textView.setText(text);
        container.addView(blockView);
    }
}