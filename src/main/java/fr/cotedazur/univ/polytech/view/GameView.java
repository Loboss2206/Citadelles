package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.logger.LamaLevel;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.DispatchState;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.PurpleEffectState;
import fr.cotedazur.univ.polytech.model.deck.Deck;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class GameView {
    private static final Logger LOGGER = Logger.getLogger(LamaLogger.class.getName());


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
    public void printPlayerAction(DispatchState action, Player player) {
        String playerNameAndRole = "Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ")";
        switch (action) {
            case TWO_GOLDS ->
                    displayMessage(playerNameAndRole + " a choisi de prendre 2 pièces d'or, il possède maintenant " + player.getGolds() + " pièces d'or.");
            case DRAW_CARD -> {
                if (player.getBoard().contains(DistrictCard.LIBRARY)) {
                    displayMessage(playerNameAndRole + " a choisi de piocher deux carte, il possède maintenant " + player.getHands().size() + " cartes dans sa main");
                } else {
                    displayMessage(playerNameAndRole + " a choisi de piocher une carte, il possède maintenant " + player.getHands().size() + " cartes dans sa main");
                }
            }
            case PLACE_DISTRICT ->
                    displayMessage(playerNameAndRole + " a choisi de placer le quartier : " + player.getBoard().get(player.getBoard().size() - 1).getDistrictName());
            case CANT_PLAY ->
                    displayMessage(playerNameAndRole + " ne peut rien faire car il n'y a plus de pieces ni de cartes :");
            default -> throw new IllegalStateException("Unexpected action: " + action);
        }
    }

    public void printPurpleEffect(Player player, PurpleEffectState purpleEffectUsed) {
        printPurpleEffect(player, null, null, purpleEffectUsed);
    }

    public void printPurpleEffect(Player player, DistrictCard districtCard, PurpleEffectState purpleEffectUsed) {
        printPurpleEffect(player, districtCard, null, purpleEffectUsed);
    }

    public void printPurpleEffect(Player player, Color color, PurpleEffectState purpleEffectUsed) {
        printPurpleEffect(player, null, color, purpleEffectUsed);
    }

    public void printPurpleEffect(Player player, DistrictCard districtCard, Color color, PurpleEffectState purpleEffectUsed) {
        String playerNameAndRole = "Le joueur " + player.getName() + " (" + (player.getPlayerRole() != null ? player.getPlayerRole().getCharacterName() : "") + ")";
        switch (purpleEffectUsed) {
            case LABORATORY_EFFECT ->
                    displayMessage(playerNameAndRole + " utilise l'effet du laboratoire pour récupérer 1 pièces en défaussant 1 carte ");
            case GRAVEYARD_EFFECT -> {
                if (districtCard != null)
                    displayMessage(playerNameAndRole + " utilise l'effet du cimetière pour récupérer " + districtCard.getDistrictName() + " venant d'être détruit ");
            }
            case KEEP_EFFECT ->
                    displayMessage(playerNameAndRole + " possèede un donjon qui ne peut pas être détruit par le warlord");
            case HAUNTED_CITY -> {
                if (color != null)
                    displayMessage(playerNameAndRole + " utilise l'effet de la cours des miracles et choisis la couleur " + color.getColorName() + " pour sa carte");
            }
            case DRAGON_GATE_EFFECT ->
                    displayMessage(playerNameAndRole + " possède la carte 'Dracoport' qui vaut maintenant 8");
            case LIBRARY_EFFECT ->
                    displayMessage(playerNameAndRole + " peut piocher 2 cartes grâce à l'effet de la carte 'Bibliothèque'");
            case UNIVERSITY_EFFECT ->
                    displayMessage(playerNameAndRole + " possède la carte 'Université' qui vaut maintenant 8");
            case OBSERVATORY_EFFECT ->
                    displayMessage(playerNameAndRole + " peut choisir entre 3 cartes lors de la pioches grâce à l'effet de la carte 'Observatoire'");
            case SCHOOL_OF_MAGIC_EFFECT -> {
                if (color != null)
                    displayMessage(playerNameAndRole + " utilise l'effet de l'école de magie et choisis la couleur " + color.getColorName() + " pour sa carte");
            }
            case SMITHY_EFFECT ->
                    displayMessage(playerNameAndRole + " utilise l'effet de la manufacture pour récupérer au mieux 3 cartes du deck en dépensant 3 pièces d'or");
            default -> displayMessage("Effet violet non connu");
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
                player.getBoard().forEach(element -> joiner.add(element.getDistrictName() + " " + ((element == DistrictCard.DRAGON_GATE || element == DistrictCard.UNIVERSITY) ? element.getDistrictValue() + 2 : element.getDistrictValue()) + " pts"));

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
        LOGGER.log(LamaLevel.VIEW, message);
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

    /**
     * print the information of all the players
     *
     * @param players the list of players
     */
    public void printRecapOfAllPlayers(List<Player> players) {
        displayMessage("---------------------Récapitulatif des joueurs------------------------");
        for (Player player : players) {
            displayMessage("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") possède " + player.getGolds() + " pièces d'or et " + player.getHands().size() + " cartes dans sa main");
        }
        displayMessage("----------------------------------------------------------------------\n");
    }

    /**
     * print when a player has been killed
     *
     * @param characterCard the role killed
     */
    public void killPlayer(CharacterCard characterCard) {
        displayMessage("Le joueur ayant le role: " + characterCard.getCharacterName() + " est mort.");
    }

    /**
     * print when a role has been stole
     *
     * @param characterCard the role stolen
     */
    public void stolenPlayer(CharacterCard characterCard) {
        displayMessage("Le joueur ayant le rôle: " + characterCard.getCharacterName() + " s'est fait voler");
    }

    /**
     * print when a player destroy the district of another player
     *
     * @param player            the player who destroyed the district
     * @param playerToDestroy   the player who lost the district
     * @param districtToDestroy the district destroyed
     */
    public void printDistrictDestroyed(Player player, Player playerToDestroy, DistrictCard districtToDestroy) {
        displayMessage("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") a détruit le quartier " + districtToDestroy.getDistrictName() + " du joueur " + playerToDestroy.getName() + " (" + playerToDestroy.getPlayerRole().getCharacterName() + ")");
    }

    /**
     * print when a player exchange his hand with another player of another player
     *
     * @param playerMagician the player who want to exchange
     * @param playerTargeted the player who underwent the exchange
     */
    public void exchangePlayerCard(Player playerMagician, Player playerTargeted) {
        displayMessage("Le joueur : " + playerMagician.getName() + " s'est fait échanger ces cartes avec : " + playerTargeted.getName());
    }

    /**
     * print when a player exchange some cards in his hands with the Deck
     *
     * @param playerMagician the player who want to exchange with the deck
     * @param cards          the cards that the player give to the deck
     */
    public void exchangeDeckCard(Player playerMagician, List<DistrictCard> cards) {
        displayMessage("Le joueur : " + playerMagician.getName() + " a échanger " + cards.size() + " cartes avec le deck");
    }

    /**
     * print the board of all the players
     *
     * @param players the list of players
     */
    public void printBoardOfAllPlayers(List<Player> players) {
        displayMessage("--------------------------Board des joueurs---------------------------");
        for (Player p : players) {
            displayMessage(p.getName() + " | board : " + p.getBoard());
        }
        displayMessage("-----------------------------------------------------------------------\n");
    }

    /**
     * print when the character earn golds
     *
     * @param playerThatWantToUseEffect the player who want to use the effect
     * @param characterColor            the color of the character
     * @param golds                     the number of golds earned
     */
    public void printCharacterGetGolds(Player playerThatWantToUseEffect, Color characterColor, int golds) {
        if (golds > 0)
            displayMessage("Le joueur " + playerThatWantToUseEffect.getName() + " recupère " + golds + " pièces d'or grâce à ses quartiers " + characterColor.getColorName());
    }

    /**
     * print when the player uses the graveyard effect
     *
     * @param playerHasGraveyard the player who uses the effect
     * @param districtToDestroy  the district destroyed
     */
    public void printGraveyardEffect(Player playerHasGraveyard, DistrictCard districtToDestroy) {
        displayMessage("Le joueur " + playerHasGraveyard.getName() + " recupère le quartier " + districtToDestroy.getDistrictName() + " grace au cimetière");
    }

    /**
     * Display the comparaison of the bots for the 2000 games
     *
     * @param winnerPerPlayer  the number of wins per player
     * @param scoringPerPlayer the scoring average per player
     */
    public void diplayBotComparaison(Map<String, Integer> winnerPerPlayer, Map<String, Integer> scoringPerPlayer) {
        for (Map.Entry<String, Integer> entry : winnerPerPlayer.entrySet()) {
            if (entry.getKey().equals("Draw")) continue;
            LOGGER.log(LamaLevel.DEMO, entry.getKey() + " : " + entry.getValue() + " wins");
        }

        LOGGER.log(LamaLevel.DEMO, "Draw " + winnerPerPlayer.get("Draw") + " times");
        for (Map.Entry<String, Integer> entry : winnerPerPlayer.entrySet()) {
            if (entry.getKey().equals("Draw")) continue;
            LOGGER.log(LamaLevel.DEMO, entry.getKey() + " : " + (entry.getValue() * 100 / 1000) + "%");
        }
        LOGGER.log(LamaLevel.DEMO, "Draw rate : " + (winnerPerPlayer.get("Draw") * 100 / 1000) + "%");
        LOGGER.log(LamaLevel.DEMO, "Scoring avg per player : " + scoringPerPlayer);
    }

    /**
     * Display the statistics of a line of the csv file
     *
     * @param line the line to display
     */
    public void displayStats(String[] line) {
        LOGGER.log(LamaLevel.DEMO, line[1] + " : ");
        LOGGER.log(LamaLevel.DEMO, "    " + line[2] + " : " + line[3]);
        for (int i = 4; i < line.length; i += 11) {
            LOGGER.log(LamaLevel.DEMO, "    " + line[i] + " -> " +
                    line[i + 1] + " : " + line[i + 2] + " | " +
                    line[i + 3] + " : " + line[i + 4] + " | " +
                    line[i + 5] + " : " + line[i + 6] + " | " +
                    line[i + 7] + " : " + line[i + 8] + " | " +
                    line[i + 9] + " : " + line[i + 10]);
        }
    }
}