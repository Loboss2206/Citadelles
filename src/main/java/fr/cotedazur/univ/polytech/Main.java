package fr.cotedazur.univ.polytech;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;

import java.util.ArrayList;

public class Main {
    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());


    public static void main(String... args) {
        LamaLogger.setupFileLog(true, "gameLog.log");
        LamaLogger.setupConsole(true,true);
        LOGGER.info("Start of the game");
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Player player = new BotRandom();
            Player player2 = new BotWeak();
            players.add(player);
            players.add(player2);
        }

        Game game = new Game(players);
        game.startGame();
    }

}
