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
    public String format(LogRecord logRecord) {
        StringBuilder builder = new StringBuilder();
        Level level = logRecord.getLevel();
        if (level == Level.INFO) {
            builder.append(ANSI_BLUE + "[INFO] ");
        } else if (level == LamaLevel.WARNING) {
            builder.append(ANSI_YELLOW + "[WARNING] ");
        } else if (level == LamaLevel.SEVERE) {
            builder.append(ANSI_RED + "[SEVERE] ");
        } else if (level == LamaLevel.FINE) {
            builder.append(ANSI_GREEN + "[FINE] ");
        }else if (level == LamaLevel.VIEW) {
            builder.append(ANSI_GREEN + "[VIEW] ");
        }else if (level == LamaLevel.DEMO) {
            builder.append(ANSI_YELLOW + "[DEMO] ");
        }
        builder.append(logRecord.getMessage());
        builder.append("\n");
        builder.append(ANSI_RESET);
        return builder.toString();
    }
}
