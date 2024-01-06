package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.CharacterDeck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    private final ArrayList<Player> players; //all the players that play in the game

    private final GameView view;
    private final PlayerComparator playerComparator;

    //Decks
    private DistrictDeck districtDeck;
    private DistrictDeck districtDiscardDeck;
    private CharacterDeck characterDeck;
    private CharacterDeck characterDiscardDeck;
    int roundNumber = 0;

    public Game(List<Player> players) {
        this.view = new GameView(this);
        //Add players
        this.players = (ArrayList<Player>) players;
        this.playerComparator = new PlayerComparator();

        int randomIndex = new Random().nextInt(players.size());
        players.get(randomIndex).setCrowned(true);

        //Build the decks and shuffle them
        buildDecks();
    }

    /**
     * Build all the decks and shuffle them
     */
    protected void buildDecks() {
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDiscardDeck = DeckFactory.createEmptyDistrictDeck();
        this.characterDeck = DeckFactory.createCharacterDeck();
        this.characterDiscardDeck = DeckFactory.createEmptyCharacterDeck();
        this.districtDeck.shuffle();
        this.characterDeck.shuffle();
    }

    /**
     * Set the crowned player as the first player of the list
     */
    public void setCrownedPlayerToFirstPlace() {
        while (!players.get(0).isCrowned()) {
            players.add(players.remove(0));
        }
    }

    /**
     * Start the game and the rounds
     */
    public void startGame() {
        //Print the greeting
        view.printStartGame();

        //Draw 4 cards of District for each player at the beginning of the game
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                player.drawCard(this.districtDeck);
            }
        }

        //Start the rounds until a player has won or the max number of rounds is reached
        do {
            //Build the decks and shuffle them
            characterDeck = DeckFactory.createCharacterDeck();
            characterDiscardDeck = DeckFactory.createEmptyCharacterDeck();

            //Set the crowned player as the first player of the list
            setCrownedPlayerToFirstPlace();

            //Start the round
            Round round = new Round(this.players, this.view, this.districtDeck, this.districtDiscardDeck, this.characterDeck, this.characterDiscardDeck, ++roundNumber);
            round.startRound();
        } while (!isGameFinished());

        // End of the game
        calculatePoints();
        sortByPoints(players);
        view.printPlayersRanking(players);
    }

    /**
     * Check if the game is finished
     *
     * @return true if a player has 8 districts or more in his board, false otherwise
     */
    public boolean isGameFinished() {
        for (Player player : players) {
            if (player.getBoard().size() >= 8) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate the points of each player
     */
    public void calculatePoints() {
        for (Player player : players) {
            int totalValueConstructed = 0;
            for(DistrictCard card : player.getBoard()){
                totalValueConstructed += card.getDistrictValue();
            }
            player.setPoints(player.getPoints() + totalValueConstructed);

            //If the player has 5 different colors
            int colorGreen = 0;
            int colorYellow = 0;
            int colorBlue = 0;
            int colorRed = 0;
            int colorPurple = 0;
            for(DistrictCard card : player.getBoard()){
                if(card.getDistrictColor() == Color.YELLOW) colorYellow++;
                if(card.getDistrictColor() == Color.GREEN) colorGreen++;
                if(card.getDistrictColor() == Color.BLUE) colorBlue++;
                if(card.getDistrictColor() == Color.RED) colorRed++;
                if(card.getDistrictColor() == Color.PURPLE) colorPurple++;
            }
            if(colorGreen > 0 && colorYellow > 0 && colorBlue > 0 && colorRed > 0 && colorPurple > 0){
                player.setPoints(player.getPoints() + 3);
            }

            //If first to complete 8 quarters
            if (player.isFirstToAdd8district()) {
                player.setPoints(player.getPoints() + 4);
            }

            //If the player complete 8 quarters
            if (player.getBoard().size() == 8) {
                player.setPoints(player.getPoints() + 2);
            }

            //Bonus off purple cards (will be implemented later)
        }
    }

    /**
     * Sort the players by their amount of points
     *
     * @param playersList the list of players to sort
     */
    public void sortByPoints(List<Player> playersList) {
        playersList.sort(playerComparator);
        Collections.reverse(playersList);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String startGameTest(){
        startGame();
        return players.get(0).getClass().getName();
    }
}
