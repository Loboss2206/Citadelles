package fr.cotedazur.univ.polytech.startingpoint.controller;

import fr.cotedazur.univ.polytech.startingpoint.model.bot.Player;
import fr.cotedazur.univ.polytech.startingpoint.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final ArrayList<Player> players;//all the players that play in the game

    private final GameView view;

    private final Round round;

    public Game(List<Player> players) {
        this.view = new GameView(this);
        //Add players
        this.players = (ArrayList<Player>) players;

        this.round = new Round(this.players, this.view);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Round getRound() {
        return round;
    }

    public void startGame() {
        view.printStartGame();
        for (int nbRound = 1; nbRound < 2; nbRound++) {
            view.printStartRound(nbRound);
            view.printEndRound(nbRound);
        }
        view.printPlayersRanking();
    }


}
