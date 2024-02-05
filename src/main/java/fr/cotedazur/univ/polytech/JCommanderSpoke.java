package fr.cotedazur.univ.polytech;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class JCommanderSpoke {
    @Parameter(names={"--demo"})
    public boolean demoMode;
    @Parameter(names={"--2thousands", "-2k"})
    public boolean twoThousands;
}
