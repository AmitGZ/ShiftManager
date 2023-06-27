package com.android.example.shiftmanagementapp.Utils;

public class ShiftData {
    private long _start;
    private long _end;
    private double _hourlyRate;
    private double _shiftSalary;
    private String _key;
    
    public ShiftData() {
        // Default constructor
    }
    
    public ShiftData(long start, long end, double hourlyRate) {
        _start = start;
        _end = end;
        _hourlyRate = hourlyRate;
        CalculateShiftSalary();
    }
    
    private void CalculateShiftSalary()
    {
        _shiftSalary = _hourlyRate * ((double)(_end - _start) / (3600.0D * 1000.0D));
    }
    
    // Getters
    public long getStart() { return _start; }
    
    public long getEnd() {
        return _end;
    }
    
    public double getHourlyRate() { return _hourlyRate; }
    
    public double getShiftSalary() { return _shiftSalary; }
    
    public String getKey() { return _key; }
    
    public void setStart(long start) { _start = start; }
    
    public void setEnd(long end) { _end = end; }
    
    public void setHourlyRate(double hourlyRate) { _hourlyRate = hourlyRate; }
    
    public void setShiftSalary(double shiftSalary) { _shiftSalary = shiftSalary; }
    
    public void setKey(String key) { _key = key; }
}
