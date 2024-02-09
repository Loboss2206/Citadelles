package fr.cotedazur.univ.polytech.logger;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

public class LamaFormatterTest {

    @Test
    public void formatsInfoLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(Level.INFO, "Test message");
        String expected = "\u001B[34m[INFO] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }

    @Test
    public void formatsWarningLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(LamaLevel.WARNING, "Test message");
        String expected = "\u001B[33m[WARNING] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }

    @Test
    public void formatsSevereLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(LamaLevel.SEVERE, "Test message");
        String expected = "\u001B[31m[SEVERE] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }

    @Test
    public void formatsFineLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(LamaLevel.FINE, "Test message");
        String expected = "\u001B[32m[FINE] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }

    @Test
    public void formatsViewLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(LamaLevel.VIEW, "Test message");
        String expected = "\u001B[32m[VIEW] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }

    @Test
    public void formatsDemoLevelLogRecordCorrectly() {
        LamaFormatter formatter = new LamaFormatter();
        LogRecord record = new LogRecord(LamaLevel.DEMO, "Test message");
        String expected = "\u001B[33m[DEMO] Test message\n\u001B[0m";
        assertEquals(expected, formatter.format(record));
    }
}