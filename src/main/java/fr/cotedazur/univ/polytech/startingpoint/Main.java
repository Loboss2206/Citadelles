package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.controller.Game;
import fr.cotedazur.univ.polytech.startingpoint.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.startingpoint.model.bot.Player;

import java.util.ArrayList;

public class Main {

    public static void main(String... args) {
        ArrayList<Player> players = new ArrayList<>();
        BotRandom botRandom1 = new BotRandom();
        players.add(botRandom1);

        Game game = new Game(players);
        game.startGame();
    }

}
