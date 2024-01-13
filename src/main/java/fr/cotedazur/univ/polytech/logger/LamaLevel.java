package fr.cotedazur.univ.polytech.logger;

import java.util.logging.Level;

public class LamaLevel extends Level {
    public static final Level HIDDEN = new LamaLevel("HIDDEN", Level.INFO.intValue() + 1);

    protected LamaLevel(String name, int value) {
        super(name, value);
    }
}