package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.EffectControl;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Round {
    private final List<Player> players;
    private final List<Player> playersSortedByCharacterNumber;
    private final GameView view;

    //Decks
    private final Deck<DistrictCard> districtDeck;
    private final Deck<DistrictCard> districtDiscardDeck; // This deck will be used when the warlord destroy a district or when the magician swap his hand with the deck
    private final Deck<CharacterCard> characterDeck;
    private final Deck<CharacterCard> faceUpCharactersDiscarded;
    private CharacterCard faceDownCharacterDiscarded;
    private final int nbRound;

    private final EffectControl effectControl;

    public Round(List<Player> players, GameView view, Deck<DistrictCard> districtDeck, Deck<DistrictCard> districtDiscardDeck, int nbRound) {
        this.players = players;
        this.playersSortedByCharacterNumber = new ArrayList<>(players);
        this.view = view;

        // Keep the decks for the districts
        this.districtDeck = districtDeck;
        this.districtDiscardDeck = districtDiscardDeck;

        // New decks for the characters
        this.characterDeck = DeckFactory.createCharacterDeck();
        characterDeck.shuffle();
        this.faceUpCharactersDiscarded = DeckFactory.createEmptyCharacterDeck();

        this.nbRound = nbRound;

        //reset the players effect boolean
        for(Player player : players){
            player.setUsedEffect("");
        }
        effectControl = new EffectControl();
    }

    /**
     * Play the round
     */
    public void startRound() {
        //Announce the start of the round
        view.printStartRound(nbRound);

        //Discard cards
        discardCards();

        //Each player choose a character
        choiceOfCharactersForEachPlayer();

        //Set the new crowned player if there is one
        setNewCrownedPlayer();

        //Print the recap of all players
        view.printRecapOfAllPlayers(players);

        //Sort the players by the number of the character card
        sortPlayersByNumbersOfCharacterCard();

        //Each player make a choice (draw a card or take 2 golds) and put a district
        choiceActionsForTheRound();

        //Announce the end of the round
        view.printEndRound(nbRound);
    }

    /**
     * Discard cards at the start of the round
     * 4 players : 2 cards face-up and 1 face-down
     * 5 players : 1 cards face-up and 1 face-down
     * 6 players : 0 card face-up and 1 face-down
     * 7 players : 0 card face-up and 1 face-down
     */
    public void discardCards() {
        int numberOfPlayers = players.size();

        if (numberOfPlayers < 6) {
            for (int i = numberOfPlayers-4; i < 2;i++){
                CharacterCard drawnCard = characterDeck.draw();

                //King can't be discarded face-up
                if (drawnCard == CharacterCard.KING) {
                    drawnCard = characterDeck.draw();
                    characterDeck.add(CharacterCard.KING);
                }

                faceUpCharactersDiscarded.add(drawnCard);
            }

            view.printDiscardedCardFaceUp(faceUpCharactersDiscarded);
        }

        //1 card has to be discarded face-down
        faceDownCharacterDiscarded = characterDeck.draw();

        view.printDiscardedCardFaceDown(faceDownCharacterDiscarded);
    }
    /**
     * Sort the players by the number of the character card
     */
    public void sortPlayersByNumbersOfCharacterCard() {
        playersSortedByCharacterNumber.sort(Comparator.comparingInt(player -> player.getPlayerRole().getCharacterNumber()));
    }

    /**
     * Function that allows each player to choose a character in the list of character available
     */
    public void choiceOfCharactersForEachPlayer() {
        int i = 0;
        for (Player player: players){
            //while the player has not chosen a character (or the character is not available)
            boolean again = true;
            while (again) {
                //Print the all character cards in the deck
                view.printPlayerPickACard(player.getName(), characterDeck.getCards());

                // Case where there is 7 players, the last player recover the face-down card to choose his character
                if (i == 6){
                    view.printCharacterCard(faceDownCharacterDiscarded.getCharacterNumber(), faceDownCharacterDiscarded.getCharacterName(), faceDownCharacterDiscarded.getCharacterEffect());
                    characterDeck.add(faceDownCharacterDiscarded);
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
                    view.printEndOfPicking(player.getName());
                }
            }
            i++;
        }
    }

    /**
     * Function that allows each player to choose their actions for the current round (choose 2golds or draw a card and choose to put a district or not)
     */
    public void choiceActionsForTheRound() {
        String choice;
        for (Player player : playersSortedByCharacterNumber) {
            //Take the choice
            choice = player.startChoice(districtDeck);
            if (choice != null) view.printPlayerAction(choice, player);

            if(player.wantToUseEffect(true)){
                this.playerWantToUseEffect(player,playersSortedByCharacterNumber);
            }

            //Because architect automatically take +2 cards
            if(player.getPlayerRole() == CharacterCard.ARCHITECT) player.useRoleEffect(Optional.of(districtDeck),Optional.empty());

            // Draw and place a district
            int i = 0;
            int maxDistrictThatCanBePut = 1;
            if(player.getPlayerRole() == CharacterCard.ARCHITECT) maxDistrictThatCanBePut = 3;
            while(i++ < maxDistrictThatCanBePut)player.drawAndPlaceADistrict(view);

            if(player.wantToUseEffect(false)){
                this.playerWantToUseEffect(player,playersSortedByCharacterNumber);
            }

            // Display the effect of the character card
            view.printCharacterUsedEffect(player);


            if(player.getBoard().size() >= 8 && noPlayerAddCompleteFirst()) player.setFirstToAdd8district(true);
            view.printEndTurnOfPlayer(player);
        }
    }

    public  void playerWantToUseEffect(Player player,List<Player> players){
        switch (player.getPlayerRole()) {
            case ASSASSIN -> {
                //TODO TO TEST
            }
            case THIEF -> {
                if(effectControl.getNbTimesEffectIsUsed().get("Steal") == 0){
                    player.useRoleEffect(Optional.empty(),Optional.of((ArrayList<Player>) effectControl.playerNeededForEffectWithoutSensibleInformation(players,player)));
                    effectControl.getNbTimesEffectIsUsed().put("Steal",1);
                }
            }
            case MAGICIAN -> {
                //TODO TO TEST
            }
            case KING -> {
                if(effectControl.getNbTimesEffectIsUsed().get("EarnDistrictKing") == 0){
                    player.useRoleEffect(Optional.empty(),Optional.empty());
                    effectControl.getNbTimesEffectIsUsed().put("EarnDistrictKing",1);
                }
            }
            case BISHOP -> {
                if(effectControl.getNbTimesEffectIsUsed().get("EarnDistrictBishop") == 0){
                    player.useRoleEffect(Optional.empty(),Optional.empty());
                    effectControl.getNbTimesEffectIsUsed().put("EarnDistrictBishop",1);
                }
            }
            case MERCHANT -> {
                if(effectControl.getNbTimesEffectIsUsed().get("EarnDistrictMerchant") == 0){
                    player.useRoleEffect(Optional.empty(),Optional.empty());
                    effectControl.getNbTimesEffectIsUsed().put("EarnDistrictMerchant",1);
                }
            }
            case WARLORD -> {
                //String warlordEffect = player.WhichWarlordEffect();

                //Replace Destroy by the name of the effect
                if(effectControl.getNbTimesEffectIsUsed().get("Destroy") == 0){
                    //WARLORD EFFECT
                    effectControl.getNbTimesEffectIsUsed().put("Destroy",1);
                }
            }
        }
    }

    public boolean noPlayerAddCompleteFirst(){
        for(Player player : players){
            if(player.isFirstToAdd8district()) return false;
        }
        return true;
    }

    /**
     * Set the crown to the new king
     */
    public void setNewCrownedPlayer() {
        boolean kingFound = false;
        for (Player player : players) {
            if (player.getPlayerRole() == CharacterCard.KING) {
                kingFound = true;
                break;
            }
        }
        if (kingFound) {
            for (Player player : players) {
                player.setCrowned(player.getPlayerRole() == CharacterCard.KING);
            }
        }
    }

    public Deck<CharacterCard> getCharacterDiscardDeck() {
        return faceUpCharactersDiscarded;
    }
}
