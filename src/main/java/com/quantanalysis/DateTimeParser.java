package com.quantanalysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {
    private final DateTimeFormatter formatter;
    private static final Logger logger = LoggerFactory.getLogger(DateTimeParser.class);

    public DateTimeParser(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public LocalDateTime parse(String date, String timeInSeconds) {
        String correctedTime = correctTimeFormat(timeInSeconds);
        String dateTimeStr = date + " " + correctedTime;

        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            logger.error("Error parsing date: {}", e.getMessage());
            return null;
        }
    }

    private String correctTimeFormat(String timeInSeconds) {
        int totalSeconds = (int) Double.parseDouble(timeInSeconds);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d%02d%02d", hours, minutes, seconds);
    }
}