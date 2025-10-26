package com.financemanager.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date operations
 */
public class DateUtils {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    /**
     * Parse a date string in yyyy-MM-dd format
     */
    public static LocalDate parseDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * Format a LocalDate to yyyy-MM-dd string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format a LocalDate for display (MMM dd, yyyy)
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Get the start of the current month
     */
    public static LocalDate getStartOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    /**
     * Get the end of the current month
     */
    public static LocalDate getEndOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }
    
    /**
     * Get the start of the current year
     */
    public static LocalDate getStartOfCurrentYear() {
        return LocalDate.now().withDayOfYear(1);
    }
    
    /**
     * Get the end of the current year
     */
    public static LocalDate getEndOfCurrentYear() {
        LocalDate now = LocalDate.now();
        return now.withDayOfYear(now.lengthOfYear());
    }
    
    /**
     * Check if a date string is valid
     */
    public static boolean isValidDate(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
