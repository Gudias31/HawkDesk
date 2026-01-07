package com.hawkdesk.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
    
    private static final SimpleDateFormat DATETIME_FORMAT = 
        new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
    
    private static final SimpleDateFormat TIME_FORMAT = 
        new SimpleDateFormat("HH:mm", new Locale("pt", "BR"));
    
    /**
     * Format date to dd/MM/yyyy
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Format date to dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return DATETIME_FORMAT.format(date);
    }
    
    /**
     * Format date to HH:mm
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        return TIME_FORMAT.format(date);
    }
    
    /**
     * Get relative time string (e.g., "Há 5 minutos")
     */
    public static String getRelativeTime(Date date) {
        if (date == null) {
            return "";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (seconds < 60) {
            return "Agora";
        } else if (minutes < 60) {
            return "Há " + minutes + " minuto" + (minutes > 1 ? "s" : "");
        } else if (hours < 24) {
            return "Há " + hours + " hora" + (hours > 1 ? "s" : "");
        } else if (days < 7) {
            return "Há " + days + " dia" + (days > 1 ? "s" : "");
        } else {
            return formatDate(date);
        }
    }
}
