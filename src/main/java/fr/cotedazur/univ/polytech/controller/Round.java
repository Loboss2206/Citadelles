package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Round {
    private final ArrayList<Player> players;

    private final GameView view;


    //Decks
    private Deck<DistrictCard> districtDeck;
    private Deck<DistrictCard> districtDiscardDeck;
    private Deck<CharacterCard> characterDeck;
    private Deck<CharacterCard> characterDiscardDeck;

    private final int nbRound;

    public Round(List<Player> players, GameView view, Deck<DistrictCard> districtDeck, Deck<DistrictCard> districtDiscardDeck, Deck<CharacterCard> characterDeck, Deck<CharacterCard> characterDiscardDeck, int nbRound) {
        this.players = (ArrayList<Player>) players;
        this.view = view;
        this.districtDeck = districtDeck;
        this.districtDiscardDeck = districtDiscardDeck;
        this.characterDeck = characterDeck;
        this.characterDiscardDeck = characterDiscardDeck;
        this.nbRound = nbRound;
    }

    public void startRound() {

        //Announce the start of the round
        view.printStartRound(nbRound);

        String result = "";

        //Each player choose a character
        for(Player player: players){
            //while the player has not chosen a character (or the character is not available)
            boolean again = true;
            while (again) {
                //Print the all character cards in the deck
                view.printPlayerPickACard(player.getName());
                for (CharacterCard character : characterDeck.getCards()) {
                    view.printCharacterCard(character.getCharacterNumber(), character.getCharacterName(), character.getCharacterDescription());
                }
                //The player choose a character from the deck
                int characterNumber = player.chooseCharacter(characterDeck.getCards());
                CharacterCard drawn = characterDeck.draw(characterNumber);
                //If the card is not available, the player choose again, after an error message
                if (drawn == null) {
                    view.pickARoleCardError();
                } else {
                    //Else, we set the role of the player and print the character card chosen
                    again = false;
                    player.setPlayerRole(drawn);
                    view.printCharacterCard(drawn.getCharacterName());
                }

            }
        }

        players.sort(Comparator.comparingInt(player->player.getPlayerRole().getCharacterNumber()));


        //Each player make a choice (draw a card or take 2 golds) and put a district
        for (Player player : players) {
            //Take the choice
            result = player.startChoice((DistrictDeck) districtDeck);
            if (result != null) view.printPlayerAction(result, player);

            //Put a district
            result = player.choiceToPutADistrict();
            if (result != null) view.printPlayerAction(result, player);
            view.printEndTurnOfPlayer(player);
        }

        //Announce the end of the round
        view.printEndRound(nbRound);

    }
}
