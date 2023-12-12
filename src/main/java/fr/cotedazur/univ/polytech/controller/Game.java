package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.CharacterDeck;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final ArrayList<Player> players; //all the players that play in the game

    private final GameView view;

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

        //Build the decks and shuffle them
        buildDecks();
    }

    protected void buildDecks() {
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDiscardDeck = DeckFactory.createEmptyDistrictDeck();
        this.characterDeck = DeckFactory.createCharacterDeck();
        this.characterDiscardDeck = DeckFactory.createEmptyCharacterDeck();
        this.districtDeck.shuffle();
        this.characterDeck.shuffle();
    }

    public List<Player> getPlayers() {
        return players;
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
            characterDeck.shuffle();
            //Start the round
            Round round = new Round(this.players, this.view, this.districtDeck, this.districtDiscardDeck, this.characterDeck, this.characterDiscardDeck,++roundNumber);
            round.startRound();
        } while (!isGameFinished());

        //Print the ranking of the game
        view.printPlayersRanking();
    }

    /**
     * Check if the game is finished
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
}
