package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Round {
    private final ArrayList<Player> players;

    private final GameView view;



    //Decks
    private final Deck<DistrictCard> districtDeck;
    private final Deck<DistrictCard> districtDiscardDeck;
    private final Deck<CharacterCard> characterDeck;
    private final Deck<CharacterCard> characterDiscardDeck;
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

        int numberOfPlayers = players.size();

        characterDeck.shuffle();

        characterDiscardDeck.add(characterDeck.draw());

        if (numberOfPlayers == 4) {
            for (int i = 0; i < 2;i++){
                CharacterCard drawnCard = characterDeck.draw();
                if (drawnCard == CharacterCard.KING) {
                    drawnCard = characterDeck.draw();
                    characterDeck.add(CharacterCard.KING);

                }
                characterDiscardDeck.add(drawnCard);

            }
            view.printDiscardedCard(characterDiscardDeck.getCards().get(1).getCharacterName(), characterDiscardDeck.getCards().get(2).getCharacterName());

        }

        if (numberOfPlayers == 5) {
            CharacterCard drawnCard = characterDeck.draw();
            if (drawnCard == CharacterCard.KING) {
                drawnCard = characterDeck.draw();
                characterDeck.add(CharacterCard.KING);
            }
            characterDiscardDeck.add(drawnCard);
            view.printDiscardedCard(drawnCard.getCharacterName());
        }



        int i = 0;
        //Each player choose a character
        for(Player player: players){
            //while the player has not chosen a character (or the character is not available)
            boolean again = true;
            while (again) {
                //Print the all character cards in the deck
                view.printPlayerPickACard(player.getName());
                for (CharacterCard character : characterDeck.getCards()) {
                    view.printCharacterCard(character.getCharacterNumber(), character.getCharacterName(), character.getCharacterEffect());
                }
                if (i == 6){
                    view.printCharacterCard(characterDiscardDeck.getCards().get(0).getCharacterNumber(), characterDiscardDeck.getCards().get(0).getCharacterName(), characterDiscardDeck.getCards().get(0).getCharacterEffect());
                }
                //The player choose a character from the deck
                if (i == 6){
                    characterDiscardDeck.add(characterDeck.getCards().get(0));
                }
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
            i++;
        }

        //Sort the players by the number of the character card
        sortPlayersByNumbersOfCharacterCard();

        //Each player make a choice (draw a card or take 2 golds) and put a district
        choiceActionsForTheRound();

        //Announce the end of the round
        view.printEndRound(nbRound);
    }

    /**
     * Sort the players by the number of the character card
     */
    public void sortPlayersByNumbersOfCharacterCard() {
        players.sort(Comparator.comparingInt(player -> player.getPlayerRole().getCharacterNumber()));
    }

    /**
     * function that allows each player to choose a character in the list of character available
     */
    public void choiceOfCharactersForEachPlayer() {
        for(Player player: players){
            //while the player has not chosen a character (or the character is not available)
            boolean again = true;
            while (again) {
                //Print the all character cards in the deck
                view.printPlayerPickACard(player.getName());
                for (CharacterCard character : characterDeck.getCards()) {
                    view.printCharacterCard(character.getCharacterNumber(), character.getCharacterName(), character.getCharacterEffect());
                }
                if (i == 6){
                    view.printCharacterCard(characterDiscardDeck.getCards().get(0).getCharacterNumber(), characterDiscardDeck.getCards().get(0).getCharacterName(), characterDiscardDeck.getCards().get(0).getCharacterEffect());
                }
                //The player choose a character from the deck
                if (i == 6){
                    characterDiscardDeck.add(characterDeck.getCards().get(0));
                }
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
            i++;
        }
    }

    /**
     * function that allows each player to choose their actions for the current round (choose 2golds or draw a card and choose to put a district or not)
     */
    public void choiceActionsForTheRound() {
        String result;
        for (Player player : players) {
            //Take the choice
            result = player.startChoice((DistrictDeck) districtDeck);
            if (result != null) view.printPlayerAction(result, player);

            // Put a district
            do {
                result = player.choiceToPutADistrict();
            } while ((result == null || player.hasCardOnTheBoard(result)) && player.hasPlayableCard());
            if (result != null && !player.hasCardOnTheBoard(result)) {
                player.addCardToBoard(result);
                view.printPlayerAction("putDistrict", player);
            }
            view.printEndTurnOfPlayer(player);
        }
    }
}
