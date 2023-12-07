package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final ArrayList<Player> players;
    private final GameView view;

    //Decks
    private Deck<DistrictCard> districtDeck;
    private Deck<DistrictCard> districtDiscardDeck;
    private Deck<CharacterCard> characterDeck;
    private Deck<CharacterCard> characterDiscardDeck;

    public Round(List<Player> players, GameView view, Deck<DistrictCard> districtDeck, Deck<DistrictCard> districtDiscardDeck, Deck<CharacterCard> characterDeck, Deck<CharacterCard> characterDiscardDeck) {
        this.players = (ArrayList<Player>) players;
        this.view = view;
    }

    public void startRound() {
        for (Player player : players) {
            //this is the place where the players played
            player.collectTwoGolds();
            view.printPlayerAction("2golds", player);
            if(player.putADisctrict()){
                view.printPlayerAction("putDistrict", player);
            }
        }
    }
}
