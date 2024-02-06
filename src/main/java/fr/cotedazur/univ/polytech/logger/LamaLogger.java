package fr.cotedazur.univ.polytech.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class LamaLogger {

    private static final java.util.logging.Logger LOGGER = Logger.getLogger("");
    private static final List<Handler> handlers = new ArrayList<>();

    private LamaLogger() {
        // This class should not be instantiated.
    }

    private static void setHandler4Formatter() {
        for (Handler h : LOGGER.getHandlers()) {
            LOGGER.removeHandler(h);
        }
        for (Handler h : handlers) {
            LOGGER.addHandler(h);
        }
    }

    public static void setupFileLog(boolean activated, String path) {
        if (activated) {
            try {
                FileHandler fileHandler = new FileHandler(path);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);
                handlers.add(fileHandler);
                setHandler4Formatter();
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create log file at " + path, e);
            }
        }
    }

    public static void setupConsole(boolean activated, boolean colorsActivated) {
        if (activated) {
            ConsoleHandler handler = new ConsoleHandler();
            if (colorsActivated) {
                LamaFormatter formatter = new LamaFormatter();
                handler.setFormatter(formatter);
            } else {
                SimpleFormatter formatter = new SimpleFormatter();
                handler.setFormatter(formatter);
            }
            handlers.add(handler);
            setHandler4Formatter();
        }
    }

    public static void mute() {
        LOGGER.setLevel(LamaLevel.OFF);
    }
}
