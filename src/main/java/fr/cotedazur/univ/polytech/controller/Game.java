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

    private final Round round;

    //Decks
    private DistrictDeck districtDeck;
    private DistrictDeck districtDiscardDeck;
    private CharacterDeck characterDeck;
    private CharacterDeck characterDiscardDeck;

    public Game(List<Player> players) {
        this.view = new GameView(this);
        //Add players
        this.players = (ArrayList<Player>) players;

        //Build the decks and shuffle them
        buildDecks();

        //Create a round
        this.round = new Round(this.players, this.view, this.districtDeck, this.districtDiscardDeck, this.characterDeck, this.characterDiscardDeck);
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

    public Round getRound() {
        return round;
    }

    public void startGame() {
        view.printStartGame();
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                player.drawCard(this.districtDeck);
            }
        }
        for (int nbRound = 1; nbRound < 2; nbRound++) {
            view.printStartRound(nbRound);
            view.printEndRound(nbRound);
        }
        view.printPlayersRanking();
    }


}
