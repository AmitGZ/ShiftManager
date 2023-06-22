package com.android.example.shiftmanagementapp;

public class ShiftData {
    private long _start;
    private long _end;
    private double _hourlyRate;
    private float _shiftSalary;
    
    public ShiftData() {
        // Default constructor
    }
    
    public ShiftData(long start, double hourlyRate) {
        _start = start;
        _end = -1; // Indicates the shift hasn't ended yet
        _hourlyRate = hourlyRate;
        _shiftSalary = 0;
    }
    
    public ShiftData(long start, long end, double hourlyRate) {
        _start = start;
        _end = end;
        _hourlyRate = hourlyRate;
        CalculateShiftSalary();
    }
    
    private void CalculateShiftSalary()
    {
        _shiftSalary = (float)_hourlyRate * ((float)(_end - _start) / (3600.0F * 1000.0F));
    }
    
    public void EndShift(long end)
    {
        _end = end;
        CalculateShiftSalary();
    }
    
    // Getters and setters (if needed)
    
    // Example getters
    public long getStart() {
        return _start;
    }
    
    public long getEnd() {
        return _end;
    }
}
