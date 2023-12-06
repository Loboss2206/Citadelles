package fr.cotedazur.univ.polytech.startingpoint.controller;

import fr.cotedazur.univ.polytech.startingpoint.model.bot.Player;
import fr.cotedazur.univ.polytech.startingpoint.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final ArrayList<Player> players;
    private final GameView view;

    public Round(List<Player> players, GameView view) {
        this.players = (ArrayList<Player>) players;
        this.view = view;
    }

    public void startRound() {
        for (Player player : players) {
            //this is the place where the players played
            player.collectTwoGolds();
            view.printPlayerAction("2golds", player);
        }
    }
}
