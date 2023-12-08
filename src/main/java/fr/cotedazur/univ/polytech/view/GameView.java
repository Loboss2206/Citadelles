package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;
import fr.cotedazur.univ.polytech.controller.Game;

import java.util.ArrayList;
import java.util.Collections;

public class GameView {

    private final PlayerComparator playerComparator;
    private final Game controller;

    public GameView(Game controller) {
        this.playerComparator = new PlayerComparator();
        this.controller = controller;
    }

    /**
     * print the announcement of the start of the game
     */
    public void printStartGame() {
        System.out.println("Début du jeu");
    }

    /**
     * print an announcement for the start of the round
     *
     * @param i the current round number
     */
    public void printStartRound(int i) {
        System.out.println("Début du round n°" + i);
        controller.getRound().startRound();
    }

    /**
     * print an announcement for the end of the round
     *
     * @param i the current round number
     */
    public void printEndRound(int i) {
        System.out.println("Fin du round n°" + i);
    }

    /**
     * print the action of the player
     *
     * @param action the action used
     * @param player the player concerned
     */
    public void printPlayerAction(String action, Player player) {
        if (action.equals("2golds"))
            System.out.println("Le joueur " + player.getName() + " a choisis de prendre 2 pièces d'or, ce qui lui fait " + player.getGolds() + " pièces d'or.");
        else if (action.equals("drawCard"))
            System.out.println("Le joueur " + player.getName() + " a choisis de piocher une carte.");
        if (action.equals("putDistrict"))
            System.out.println("Le joueur " + player.getName() + " a choisis de placer " + player.getBoard().get(player.getBoard().size() - 1));
    }

    /**
     * Print the ranking of the game but only with golds
     */
    public void printPlayersRanking() {
        System.out.println("Fin du jeu, voici le classement :");
        ArrayList<Player> listOfPlayers = (ArrayList<Player>) controller.getPlayers();
        listOfPlayers.sort(playerComparator);
        Collections.reverse(listOfPlayers);
        int i = 0;
        for (Player player : listOfPlayers) {
            if (player.getBoard().size() > 0) System.out.println(++i + " : " + player.getName() + ", golds = " + player.getGolds() + ", quartiers placés = " + player.getBoard().get(0).getDistrictName());
            else System.out.println(++i + " : " + player.getName() + ", golds = " + player.getGolds() + ", quartiers placés = aucun");

        }
    }
}
