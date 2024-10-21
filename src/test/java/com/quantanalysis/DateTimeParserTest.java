package com.quantanalysis;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DateTimeParserTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    void testParseValidDateTime() {
        DateTimeParser parser = new DateTimeParser("yyyyMMdd HHmmss");
        LocalDateTime result = parser.parse("20231009", "3661");
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2023, 10, 9, 1, 1, 1), result);
    }

    @Test
    void testParseInvalidDateTime() {
        DateTimeParser parser = new DateTimeParser("yyyyMMdd HHmmss");
        LocalDateTime result = parser.parse("invalid", "3661");
        assertNull(result);
    }
}
