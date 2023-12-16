package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;

import java.util.List;
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
        System.out.println("----------------------------------------------------------------------\n");
        System.out.println("Début du round n°" + i);
    }

    /**
     * print an announcement for the end of the round
     *
     * @param i the current round number
     */
    public void printEndRound(int i) {
        System.out.println("Fin du round n°" + i + "\n");
    }

    /**
     * print the action of the player
     *
     * @param action the action used
     * @param player the player concerned
     */
    public void printPlayerAction(String action, Player player) {
        String playerString = "Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ")";
        if (action.equals("2golds"))
            System.out.println(playerString + " a choisi de prendre 2 pièces d'or, il possède maintenant " + player.getGolds() + " pièces d'or.");
        else if (action.equals("drawCard"))
            System.out.println(playerString + " a choisi de piocher une carte, il possède maintenant " + player.getHands().size() + " cartes dans sa main");
        else if (action.equals("putDistrict"))
            System.out.println(playerString + " a choisi de placer le quartier : " + player.getBoard().get(player.getBoard().size() - 1).getDistrictName());
    }

    public void printEndTurnOfPlayer(Player player) {
        System.out.println("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") décide de terminer son tour");
    }

    /**
     * Print the ranking of the game but only with golds
     */
    public void printPlayersRanking(List<Player> listOfPlayers) {
        System.out.println("----------------------------------------------------------------------\n");
        System.out.println("Fin du jeu, voici le classement :");
        int i = 0;
        Player previousPlayer = null;
        for (Player player : listOfPlayers) {
            if (previousPlayer != null && playerComparator.compare(previousPlayer, player) == 0) i--;
            if (!player.getBoard().isEmpty()) {
                //For adding all the districts placed by the player
                StringJoiner joiner = new StringJoiner(", ");
                player.getBoard().forEach(element -> joiner.add(String.valueOf(element.getDistrictName())));
                System.out.println(++i + " : " + player.getName() + " " + player.getPoints() + "pts, golds = " + player.getGolds() + ", quartiers placés = " + joiner);
            }
            previousPlayer = player;
        }
    }

    /**
     * Show the player that the character card he chose is not available
     */
    public void pickARoleCardError() {
        displayMessage("Aucun personnage avec ce nombre, Réessayez");
    }

    /**
     * Print the message
     *
     * @param message the message to display
     */
    private void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Print the details of the character card
     *
     * @param number      the number of the character card
     * @param name        the name of the character card
     * @param description the description of the character card
     */
    public void printCharacterCard(int number, String name, String description) {
        displayMessage(number + " " + name + " : " + description);
    }

    /**
     * print the announcement for the player to pick a character card
     *
     * @param name the name of the player
     */
    public void printPlayerPickACard(String name) {
        displayMessage("Le joueur " + name + " est entrain de choisir une carte personnage");
    }

    /**
     * print the name of the character card picked by the player
     *
     * @param name the name of the character card
     */
    public void printCharacterCard(String name) {
        displayMessage("Vous avez choisi la carte personnage : " + name + "\n");
    }

    public void printDiscardedCard(String name) {
        displayMessage("la carte defaussé est : " + name + "\n");
    }

    public void printDiscardedCard(String name1, String name2) {
        displayMessage("les cartes defaussés sont : " + name1 + " et " + name2 + "\n");
    }
}