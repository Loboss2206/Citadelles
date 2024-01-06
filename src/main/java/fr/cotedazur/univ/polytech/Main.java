package fr.cotedazur.univ.polytech;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;

import java.util.ArrayList;

public class Main {

    public static void main(String... args) {
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
