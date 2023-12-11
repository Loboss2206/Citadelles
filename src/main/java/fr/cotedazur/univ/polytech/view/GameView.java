package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;
import fr.cotedazur.univ.polytech.controller.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

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
        System.out.println("Début du jeu\n");
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
        System.out.println("Fin du round n°" + i +"\n");
    }

    /**
     * print the action of the player
     *
     * @param action the action used
     * @param player the player concerned
     */
    public void printPlayerAction(String action, Player player) {
        if (action.equals("2golds"))
            System.out.println("Le joueur " + player.getName() + " a choisis de prendre 2 pièces d'or, il possède maintenant " + player.getGolds() + " pièces d'or.");
        else if (action.equals("drawCard"))
            System.out.println("Le joueur " + player.getName() + " a choisis de piocher une carte, il possède maintenant " + player.getHands().size() + " cartes dans sa main");
        if (action.equals("putDistrict"))
            System.out.println("Le joueur " + player.getName() + " a choisis de placer " + player.getBoard().get(player.getBoard().size() - 1));
    }

    public void printEndTurnOfPlayer(Player player) {
        System.out.println("Le joueur "+player.getName() + " décide de terminer son tour");
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
        Player previousPlayer = null;
        for (Player player : listOfPlayers) {
            if (previousPlayer != null && playerComparator.compare(previousPlayer, player) == 0) i--;
            if (!player.getBoard().isEmpty()) {
                StringJoiner joiner = new StringJoiner(", ");
                player.getBoard().forEach(element -> joiner.add(String.valueOf(element)));
                System.out.println(++i + " : " + player.getName() + ", golds = " + player.getGolds() + ", quartiers placés = " + joiner);
            }else {
                System.out.println(++i + " : " + player.getName() + ", golds = " + player.getGolds() + ", quartiers placés = aucun");
            }
            previousPlayer = player;
        }
    }
}
