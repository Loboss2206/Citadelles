package fr.cotedazur.univ.polytech.logger;


import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LamaFormatter extends Formatter {
    // ANSI escape code
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREY = "\u001B[37m";

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        Level level = record.getLevel();
        if (level == Level.INFO) {
            builder.append(ANSI_BLUE + "[INFO] ");
        } else if (level == Level.WARNING) {
            builder.append(ANSI_YELLOW + "[WARNING] ");
        } else if (level == Level.SEVERE) {
            builder.append(ANSI_RED + "[SEVERE] ");
        } else if (level == Level.FINE) {
            builder.append(ANSI_GREEN + "[FINE] ");
        }
        builder.append(record.getMessage());
        builder.append("\n");
        builder.append(ANSI_RESET);
        return builder.toString();
    }
}
