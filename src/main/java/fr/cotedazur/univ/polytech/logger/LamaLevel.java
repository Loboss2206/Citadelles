package fr.cotedazur.univ.polytech.logger;

import java.util.logging.Level;

public class LamaLevel extends Level {
    public static final Level VIEW = new LamaLevel("VIEW", Level.INFO.intValue() +1);
    public static final Level DEMO = new LamaLevel("DEMO", Level.OFF.intValue()-1);
    protected LamaLevel(String name, int value) {
        super(name, value);
    }
}
