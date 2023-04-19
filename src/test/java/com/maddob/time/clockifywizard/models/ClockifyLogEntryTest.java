package com.maddob.time.clockifywizard.models;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Duration;
import org.junit.jupiter.api.Test;

public class ClockifyLogEntryTest {
    @Test
    void testGetJiraTicketNumberFromDescription() {
        var logEntry = new ClockifyLogEntry(
            "TEST",
            "TEST CLIENT",
            "FISCO-123 some additional text here",
            "",
            "Test User",
            "",
            "test@mail.com",
            "tags",
            "true",
            LocalDate.now(),
            LocalTime.now(),
            LocalDate.now(),
            LocalTime.now(),
            Duration.ofMinutes(100),
            0.1,
            "",
            ""
        );

        assertEquals("FISCO-123", logEntry.getJiraTicketNumber());
    }

    @Test
    void testGetJiraTicketNumberFromDescriptionLowercase() {
        var logEntry = new ClockifyLogEntry(
            "TEST",
            "TEST CLIENT",
            "fisco-123 some additional text here",
            "",
            "Test User",
            "",
            "test@mail.com",
            "tags",
            "true",
            LocalDate.now(),
            LocalTime.now(),
            LocalDate.now(),
            LocalTime.now(),
            Duration.ofMinutes(100),
            0.1,
            "",
            ""
        );

        assertEquals("FISCO-123", logEntry.getJiraTicketNumber());
    }

    @Test
    void testGetJiraTicketNumberFromTask() {
        var logEntry = new ClockifyLogEntry(
            "TEST",
            "TEST CLIENT",
            "",
            "FISCO-123 hello world",
            "Test User",
            "",
            "test@mail.com",
            "tags",
            "true",
            LocalDate.now(),
            LocalTime.now(),
            LocalDate.now(),
            LocalTime.now(),
            Duration.ofMinutes(100),
            0.1,
            "",
            ""
        );

        assertEquals("FISCO-123", logEntry.getJiraTicketNumber());
    }

    @Test
    void testGetJiraTicketNumberFromTaskLowercase() {
        var logEntry = new ClockifyLogEntry(
            "TEST",
            "TEST CLIENT",
            "",
            "fisco-123 hello world",
            "Test User",
            "",
            "test@mail.com",
            "tags",
            "true",
            LocalDate.now(),
            LocalTime.now(),
            LocalDate.now(),
            LocalTime.now(),
            Duration.ofMinutes(100),
            0.1,
            "",
            ""
        );

        assertEquals("FISCO-123", logEntry.getJiraTicketNumber());
    }
}
