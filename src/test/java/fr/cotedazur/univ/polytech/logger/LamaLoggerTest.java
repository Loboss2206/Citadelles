package fr.cotedazur.univ.polytech.logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.logging.*;

import static fr.cotedazur.univ.polytech.logger.LamaLogger.LOGGER;
import static org.junit.jupiter.api.Assertions.*;


public class LamaLoggerTest {
    @BeforeEach
    public void setup() {
        for (Handler h : LOGGER.getHandlers()) {
            LOGGER.removeHandler(h);
        }
        LamaLogger.setupConsole(true, true);
        LamaLogger.setupFileLog(true, "game.log");

    }

    @AfterEach
    public void teardown() {
        LamaLogger.mute();
    }

    @Test
    public void logsMessageToConsoleWhenConsoleLoggingIsActivated() {
        LOGGER.log(Level.INFO, "Test message");
        // Check your console output to verify the log message
    }

    @Test
    public void doesNotLogMessageToConsoleWhenConsoleLoggingIsDeactivated() {
        LamaLogger.setupConsole(false, true);
        LOGGER.log(Level.INFO, "Test message");
        // Check your console output to verify the absence of the log message
    }

    @Test
    public void logsMessageToFileWhenFileLoggingIsActivated() {
        LamaLogger.setupFileLog(true, "test.log");
        LOGGER.log(Level.INFO, "Test message");
        // Check the content of "test.log" to verify the log message
    }

    @Test
    public void doesNotLogMessageToFileWhenFileLoggingIsDeactivated() {
        LamaLogger.setupFileLog(false, "test.log");
        LOGGER.log(Level.INFO, "Test message");
        // Check the content of "test.log" to verify the absence of the log message
    }

    @Test
    public void doesNotLogMessageWhenLoggerIsMuted() {
        LamaLogger.mute();
        LOGGER.log(Level.INFO, "Test message");
        // Check your console output and "test.log" to verify the absence of the log message
    }

    @Test
public void setsLamaFormatterWhenColorsActivated() {
    LamaLogger.setupConsole(true, true);
    Handler handler = LOGGER.getHandlers()[0];
        assertInstanceOf(LamaFormatter.class, handler.getFormatter());
}

@Test
public void setsSimpleFormatterWhenColorsNotActivated() {
    LamaLogger.setupConsole(true, false);
    Handler handler = LOGGER.getHandlers()[1];
    assertInstanceOf(SimpleFormatter.class, handler.getFormatter());
}

@Test
public void doesNotSetFormatterWhenConsoleNotActivated() {
    LamaLogger.setupConsole(false, true);
    assertEquals(19, LOGGER.getHandlers().length);
}

@Test
public void logsToFileWhenFileLogActivated() {
    String path = "test.log";
    LamaLogger.setupFileLog(true, path);
    Handler handler = LOGGER.getHandlers()[1];
    for (Handler h : LOGGER.getHandlers()) {
        System.out.println(h.getClass().getName())  ;
    }
    assertInstanceOf(FileHandler.class, handler);
    assertInstanceOf(SimpleFormatter.class, handler.getFormatter());
}

@Test
public void doesNotLogToFileWhenFileLogNotActivated() {
    String path = "test.log";
    LamaLogger.setupFileLog(false, path);
    assertEquals(12, LOGGER.getHandlers().length);
}

@Test
public void throwsExceptionWhenFileCannotBeCreated() {
    String path = "/invalid/path/test.log";
    assertThrows(IllegalStateException.class, () -> LamaLogger.setupFileLog(true, path));
}
}