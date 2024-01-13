package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;

import java.util.List;
import java.util.StringJoiner;

public class GameView {

    /**
     * print the announcement of the start of the game
     */
    public void printStartGame() {
        displayMessage("Début du jeu\n");
    }

    /**
     * print an announcement for the start of the round
     *
     * @param i the current round number
     */
    public void printStartRound(int i) {
        displayMessage("----------------------------------------------------------------------\n");
        displayMessage("Début du round n°" + i);
    }

    /**
     * print an announcement for the end of the round
     *
     * @param i the current round number
     */
    public void printEndRound(int i) {
        displayMessage("Fin du round n°" + i + "\n");
    }

    /**
     * print the action of the player
     *
     * @param action the action used
     * @param player the player concerned
     */
    public void printPlayerAction(String action, Player player) {
        String playerNameAndRole = "Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ")";
        switch (action) {
            case "2golds" ->
                    displayMessage(playerNameAndRole + " a choisi de prendre 2 pièces d'or, il possède maintenant " + player.getGolds() + " pièces d'or.");
            case "drawCard" ->
                    displayMessage(playerNameAndRole + " a choisi de piocher une carte, il possède maintenant " + player.getHands().size() + " cartes dans sa main");
            case "putDistrict" ->
                    displayMessage(playerNameAndRole + " a choisi de placer le quartier : " + player.getBoard().get(player.getBoard().size() - 1).getDistrictName());
            default -> throw new IllegalStateException("Unexpected action: " + action);
        }
    }

    /**
     * Print a message when a player end his turn
     *
     * @param player the player who was playing
     */
    public void printEndTurnOfPlayer(Player player) {
        displayMessage("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") termine son tour\n");
    }

    /**
     * Print the ranking of the game
     *
     * @param listOfPlayers the list of players already sorted by the game controller
     */
    public void printPlayersRanking(List<Player> listOfPlayers) {
        displayMessage("----------------------------------------------------------------------\n");
        displayMessage("Fin du jeu, voici le classement :");
        int i = 0;//Used for ranking the player
        for (Player player : listOfPlayers) {
            //For adding all the districts placed by the player
            StringJoiner joiner = new StringJoiner(", ");
            if (!player.getBoard().isEmpty())
                player.getBoard().forEach(element -> joiner.add(element.getDistrictName() + " " + element.getDistrictValue() + " pts"));

            //Print the player's ranking and his state at the end of the game
            displayMessage(++i + " : " + player.getName() + " " + player.getPoints() + "pts, Golds = " + player.getGolds() + ", Quartiers placés = { " + joiner + " }, Personnage avant fin de la partie = " + player.getPlayerRole());
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
    public void printPlayerPickACard(String name, List<CharacterCard> charactersCards) {
        displayMessage("Le joueur " + name + " est entrain de choisir une carte personnage parmi : ");
        StringJoiner joiner = new StringJoiner(" - ");
        for (CharacterCard c : charactersCards) {
            joiner.add(c.getCharacterName());
        }
        displayMessage(joiner.toString());
    }

    /**
     * Print a message to notify that the player take an character card
     *
     * @param name the name of the character card
     */
    public void printEndOfPicking(String name) {
        displayMessage("Le joueur " + name + " a choisi une carte personnage\n");
    }

    /**
     * print the name of the character card or cards discarded face-up
     *
     * @param discard the deck of discarded cards face-up
     */
    public void printDiscardedCardFaceUp(Deck<CharacterCard> discard) {
        if (discard.size() == 1) {
            displayMessage("\nCarte defaussée face visible : " + discard.getCards().get(0).getCharacterName());
        } else {
            displayMessage("\nCartes defaussées visibles : " + discard.getCards().get(0).getCharacterName() + " et " + discard.getCards().get(1).getCharacterName());
        }
    }

    /**
     * print the name of the character card or cards discarded face-down
     *
     * @param discard the deck of discarded cards face-down
     */
    public void printDiscardedCardFaceDown(CharacterCard discard) {
        displayMessage("Carte defaussée face cachée : " + discard.getCharacterName() + "\n");
    }

    /**
     * print the announcement for the player effect (if he uses it)
     *
     * @param player the player concerned (passed to be able to print whatever needed info in the future)
     */
    public void printCharacterUsedEffect(Player player) {
        if (!player.getUsedEffect().isEmpty()) {
            String[] split = player.getUsedEffect().split("_");
            String characterName = split[0];
            String effectName = split[1];
            String playerName = "Le joueur " + player.getName();
            // Special case for the architect
            if (characterName.equals("ARCHITECT")) {
                switch (effectName) {
                    case "drawDistrict" ->
                            displayMessage(playerName + " utilise l'effet de l' l'architecte et pioche 2 cartes.");
                    case "placeDistrict" ->
                            displayMessage(playerName + " utilise l'effet de l'architecte et peut placer 3 quartiers.");
                    default -> throw new IllegalStateException("Unexpected value : " + effectName);
                }
            } else {
                //General case
                displayMessage(playerName + " utilise l'effet de/du " + characterName.toLowerCase() + ".");
            }
        }
    }

    public void printRecapOfAllPlayers(List<Player> players) {
        System.out.println("---------------------Récapitulatif des joueurs : ---------------------");
        for (Player player : players) {
            displayMessage("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") possède " + player.getGolds() + " pièces d'or et " + player.getHands().size() + " cartes dans sa main");
        }
        System.out.println("----------------------------------------------------------------------\n");
    }

    public void killPlayer(Player player1) {
        displayMessage("Le joueur ayant le role: " + player1.getPlayerRole().getCharacterName() + " est mort.");
    }
}