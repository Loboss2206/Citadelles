package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;

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
                player.getBoard().forEach(element -> joiner.add(String.valueOf(element.getDistrictName()) +" "+element.getDistrictValue()+" pts"));
                System.out.println(++i + " : " + player.getName() + " " + player.getPoints() + "pts, golds = " + player.getGolds() + ", quartiers placés = " + joiner + ", dernier perso = " + player.getPlayerRole());
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

    /**
     * print the name of the character card discarded face-up
     *
     * @param discard the deck of discarded cards
     */
    public void printDiscardedCard(Deck<CharacterCard> discard) {
        if (discard.size()==1){
            displayMessage("la carte defaussé est : " + discard.getCards().get(0).getCharacterName() + "\n");
        }
        else {
            displayMessage("les cartes defaussés sont : " + discard.getCards().get(0).getCharacterName() + " et " + discard.getCards().get(1).getCharacterName() + "\n");
        }
    }

    /**
     * print the announcement for the player effect (if he uses it)
     * @param player the player concerned (passed to be able to print whatever needed info in the future)
     */
    public void printCharacterUsedEffect(Player player) {
        if (player.getUsedEffect().isEmpty()) return;
        String split[] = player.getUsedEffect().split("_");
        String characterName = split[0];
        String effectName = split[1];
        // Special case for the architect
        if (characterName.equals("ARCHITECT")) {
            switch (effectName) {
                case "drawDistrict":
                    displayMessage("Le joueur " + player.getName() + " utilise l'effet de l' l'architecte et pioche 2 cartes.");
                    break;
                case "placeDistrict":
                    displayMessage("Le joueur" + player.getName() + " utilise l'effet de l'architecte et peut placer 3 quartiers.");
                    break;
            }
        }else {
            //General case
            displayMessage("Le joueur" + player.getName() + " utilise l'effet de/du " + characterName.toLowerCase()+".");
        }
    }
}