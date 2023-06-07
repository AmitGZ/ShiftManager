package com.android.example.shiftmanagementapp;

public class UserData {
    private String userId;
    private long timestamp;
    
    public UserData() {
        // Default constructor required for Firebase
    }
    
    public UserData(String userId, long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }
    
    // Getters and setters (if needed)
    
    // Example getters
    public String getUserId() {
        return userId;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
