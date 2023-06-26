package com.android.example.shiftmanagementapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class DateUtils {
    private static final SimpleDateFormat dateFormat;
    
    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2")); // Set your desired time zone here
    }
    
    public static String formatDateTime(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
    
    public static String getTimeDifference(long start, long end) {
        
        long seconds = (end - start) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
    
        seconds = seconds % 60;
        minutes = minutes % 60;
    
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
    
        return formattedTime;
    }
}
