package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
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
        this.districtDeck = districtDeck;
        this.districtDiscardDeck = districtDiscardDeck;
        this.characterDeck = characterDeck;
        this.characterDiscardDeck = characterDiscardDeck;
    }

    public void startRound() {
        String result = "";
        for (Player player : players) {
            //this is the place where the players played
            result = player.startChoice((DistrictDeck) districtDeck);
            if (result != null) view.printPlayerAction(result, player);

            result = player.choiceToPutADistrict();
            if (result != null) view.printPlayerAction(result, player);
        }
    }
}
