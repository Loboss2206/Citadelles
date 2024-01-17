package fr.cotedazur.univ.polytech;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;

public class Main {
    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    public static void main(String... args) {

        // Logger setup
        LamaLogger.setupFileLog(true, "game.log");
        LamaLogger.setupConsole(true,true);
        LOGGER.setLevel(java.util.logging.Level.INFO); // Change to Level.OFF to disable the logger or Level.INFO to enable it

        // View setup
        GameView view = new GameView();
        view.noDisplay(false); // Set to true to disable the display of the game (having both Logger and Display is not recommended for understanding reasons (the two displays are not synchronized))

        // Players setup
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Player player = new BotRandom();
            Player player2 = new BotWeak();
            players.add(player);
            players.add(player2);
        }

        // Game setup
        Game game = new Game(players,view);

        // Start the game
        game.startGame();
    }

}
