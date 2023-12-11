package fr.cotedazur.univ.polytech;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;

import java.util.ArrayList;

public class Main {

    public static void main(String... args) {
        ArrayList<Player> players = new ArrayList<>();
        for(int i = 0;i<4;i++){
            Player player = new BotRandom();
            players.add(player);
        }

        Game game = new Game(players,9);
        game.startGame();
    }

}
