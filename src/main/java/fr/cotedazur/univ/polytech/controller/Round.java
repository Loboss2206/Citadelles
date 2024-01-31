package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.DispatchState;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.golds.StackOfGolds;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;

public class Round {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());
    private final EffectController effectController;
    private List<Player> players;
    private List<Player> playersSortedByCharacterNumber;
    private GameView view;
    //Decks
    private Deck<DistrictCard> districtDeck;
    private Deck<DistrictCard> districtDiscardDeck; // This deck will be used when the warlord destroy a district or when the magician swap his hand with the deck
    private Deck<CharacterCard> characterDeck;
    private Deck<CharacterCard> faceUpCharactersDiscarded;
    private CharacterCard faceDownCharacterDiscarded;
    private int nbRound;
    private StackOfGolds stackOfGolds;

    public Round(List<Player> players, GameView view, Deck<DistrictCard> districtDeck, Deck<DistrictCard> districtDiscardDeck, int nbRound, StackOfGolds stackOfGolds) {
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

        this.stackOfGolds = stackOfGolds;

        //reset the players effect boolean
        for (Player player : players) {
            player.setUsedEffect("");
            player.setDead(false);
            player.setHasBeenStolen(false);
        }

        effectController = new EffectController();
        effectController.setView(view);
        effectController.setStackOfCoins(stackOfGolds);
    }

    public Round() {
        effectController = new EffectController(view, stackOfGolds);
    }

    /**
     * Play the round
     */
    public void startRound() {
        //Announce the start of the round
        LOGGER.info("Début du round " + nbRound);
        view.printStartRound(nbRound);

        //Discard cards
        discardCards();

        //Each player choose a character
        choiceOfCharactersForEachPlayer();

        //Set the new crowned player if there is one
        setNewCrownedPlayer();

        //Print the recap of all players
        view.printRecapOfAllPlayers(players);

        //Print the board of all players
        view.printBoardOfAllPlayers(players);

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
            for (int i = numberOfPlayers - 4; i < 2; i++) {
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
        LOGGER.info("Carte defaussée face cachée : " + faceDownCharacterDiscarded.getCharacterName());
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
        for (Player player : players) {
            //while the player has not chosen a character (or the character is not available)
            boolean again = true;
            while (again) {
                //Print the all character cards in the deck
                view.printPlayerPickACard(player.getName(), characterDeck.getCards());

                // Case where there is 7 players, the last player recover the face-down card to choose his character
                if (i == 6) {
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
        for (Player player : playersSortedByCharacterNumber) {
            if (player.isDead()) {
                LOGGER.info("Le joueur " + player.getName() + " est mort, il ne peut pas jouer");
                continue;
            }

            //If player is dead he will not be stolen
            if (player.isStolen()) {
                effectController.getPlayerWhoStole().getPlayerRole().useEffectThief(effectController.getPlayerWhoStole(), player, true);
            }

            this.drawOr2golds(player);

            // We play the district cards effects
            playDistrictCards(player);

            // We play the character cards effects before the player put a district
            playerWillingToUseEffect(player, true);

            // Draw and place a district
            int i = 0;
            int maxDistrictThatCanBePut = 1;
            if (player.getPlayerRole() == CharacterCard.ARCHITECT) maxDistrictThatCanBePut = 3;
            while (i++ < maxDistrictThatCanBePut) {
                this.putDistrictForPlayer(player);
            }

            // We play the character cards effects after the player put a district
            playerWillingToUseEffect(player, false);

            // Display the effect of the character card
            view.printCharacterUsedEffect(player);

            determineFirstPlayerTo8Districts(player);
            view.printEndTurnOfPlayer(player);
        }
    }

    /**
     * Function that allows each player to play his district cards
     *
     * @param player the player who will play his district cards
     */
    private void playDistrictCards(Player player) {
        //TODO verify SMITHY effect when 2 cards in deck
        if (player.hasCardOnTheBoard(DistrictCard.SMITHY) && player.getGolds() >= 3 && districtDeck.size() >= 3 && (player.wantsToUseSmithyEffect())) {
            view.printPurpleEffect(player);
            player.setGolds(player.getGolds() - 3);
            stackOfGolds.addGoldsToStack(3);
            player.addCardToHand(districtDeck.draw());
            player.addCardToHand(districtDeck.draw());
            player.addCardToHand(districtDeck.draw());
        }

        //Because architect automatically take +2 cards
        if (player.getPlayerRole() == CharacterCard.ARCHITECT)
            player.getPlayerRole().useEffectArchitect(player, districtDeck);

        //Because Merchant automatically take +1 gold
        if (player.getPlayerRole() == CharacterCard.MERCHANT)
            player.setGolds(player.getGolds() + stackOfGolds.takeAGold());

        // If the player has a laboratory, he can discard a card to earn 1 gold
        if (player.hasCardOnTheBoard(DistrictCard.LABORATORY)) {
            player.getHands().remove(player.chooseHandCardToDiscard());
            player.setGolds(player.getGolds() + stackOfGolds.takeAGold());
            view.printPurpleEffectLaboratory(player,DistrictCard.LABORATORY);
        }

        // If the player has the haunted city, we set the round where he put the haunted city
        if (player.hasCardOnTheBoard(DistrictCard.HAUNTED_CITY) && player.getWhatIsTheRoundWhereThePlayerPutHisHauntedCity() == 0)
            player.setWhatIsTheRoundWhereThePlayerPutHisHauntedCity(nbRound);
    }

    /**
     * Set the first player to 8 districts by checking if the player has 8 districts and if no player has already been set as first to 8 districts
     *
     * @param player the player to check
     */
    private void determineFirstPlayerTo8Districts(Player player) {
        if (player.getBoard().size() >= 8 && noPlayerAddCompleteFirst()) player.setFirstToAdd8district(true);
    }

    /**
     * Function that allows each player to choose if he wants to use his effect
     *
     * @param player                 the player who will use his effect
     * @param beforePuttingADistrict true if the player want to use his effect before putting a district, false otherwise
     */
    private void playerWillingToUseEffect(Player player, boolean beforePuttingADistrict) {
        if (player.wantToUseEffect(beforePuttingADistrict) && player.getPlayerRole() != CharacterCard.ARCHITECT) {
            effectController.playerWantToUseEffect(player, playersSortedByCharacterNumber, districtDiscardDeck, districtDeck);
            if (player.getPlayerRole() == CharacterCard.WARLORD)
                effectController.playerWantToUseEffect(player, playersSortedByCharacterNumber, districtDiscardDeck, districtDeck);
        }
    }

    /**
     * Function that allows each player to choose if he wants to draw a card or take 2 golds
     *
     * @param player the player who will choose to draw a card or take 2 golds
     */
    public void drawOr2golds(Player player) {
        DispatchState choice = null;

        //Take the choice
        while (choice == null) {
            choice = player.startChoice();
            if (choice.equals(DispatchState.DRAWCARD) && districtDeck.isEmpty()) choice = DispatchState.TWOGOLDS;
            if (choice.equals(DispatchState.TWOGOLDS) && stackOfGolds.getNbGolds() == 0)
                choice = DispatchState.CANTPLAY;
        }

        //Process the choice
        if (choice.equals(DispatchState.TWOGOLDS)) {
            this.collectTwoGoldsForPlayer(player);
        } else if (choice.equals(DispatchState.DRAWCARD)) {
            playerWantToDrawCard(player);
        }
        view.printPlayerAction(choice, player);
    }

    public void playerWantToDrawCard(Player player) {
        ArrayList<DistrictCard> cardsThatPlayerDraw = new ArrayList<>();

        int nbCardToDraw = player.getBoard().contains(DistrictCard.OBSERVATORY) ? 3 : 2;
        if(nbCardToDraw == 3) LOGGER.info(player.getName() + " choisis entre 3 cartes grâce à l'effet de l'observatoire");
        for (int i = 0; i < nbCardToDraw; i++) {
            if (!districtDeck.isEmpty()) cardsThatPlayerDraw.add(districtDeck.draw());
        }

        Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant = new EnumMap<>(DispatchState.class);
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDSWANTED, new ArrayList<>());
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDSNOTWANTED, new ArrayList<>());

        //If maybe there is only one card in the deck so the bot just take one card
        if (cardsThatPlayerDraw.size() == 3) {
            player.drawCard(cardsThatThePlayerDontWantAndThatThePlayerWant, cardsThatPlayerDraw.get(0), cardsThatPlayerDraw.get(1), cardsThatPlayerDraw.get(2));
        } else if (cardsThatPlayerDraw.size() == 2) {
            player.drawCard(cardsThatThePlayerDontWantAndThatThePlayerWant, cardsThatPlayerDraw.get(0), cardsThatPlayerDraw.get(1));
        } else {
            player.drawCard(cardsThatThePlayerDontWantAndThatThePlayerWant, cardsThatPlayerDraw.get(0));
        }

        if (cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSWANTED).size() == 1 || (cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSWANTED).size() == 2 && player.getBoard().contains(DistrictCard.LIBRARY))) {
            if(player.getBoard().contains(DistrictCard.LIBRARY)) LOGGER.info(player.getName() + " récupère 2 cartes dans sa main grâce à l'effet de la bibliothèque");
            player.getHands().addAll(cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSWANTED));
        }

        //Return the cards that the bot did not choose to the hand
        if (!cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSNOTWANTED).isEmpty()) {
            for (DistrictCard card : cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSNOTWANTED)) {
                districtDeck.add(card);
            }
        }
    }

    public boolean noPlayerAddCompleteFirst() {
        for (Player player : players) {
            if (player.isFirstToAdd8district()) return false;
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

    public void collectTwoGoldsForPlayer(Player player) {
        int nbMaxCoins = 0;
        for (int i = 0; i < 2; i++) {
            nbMaxCoins += stackOfGolds.takeAGold();
        }
        player.setGolds(player.getGolds() + nbMaxCoins);
    }

    public void putDistrictForPlayer(Player player) {
        DistrictCard districtToPut;
        do {
            districtToPut = player.choiceHowToPlayDuringTheRound();
        } while (player.hasCardOnTheBoard(districtToPut) && player.hasPlayableCard());
        if (districtToPut != null && !player.hasCardOnTheBoard(districtToPut)) {
            player.addCardToBoard(districtToPut);
            player.removeGold(districtToPut.getDistrictValue());
            this.stackOfGolds.addGoldsToStack(districtToPut.getDistrictValue());
            if (view != null) view.printPlayerAction(DispatchState.PLACEDISTRICT, player);
        }
    }

    public StackOfGolds getStackOfGolds() {
        return stackOfGolds;
    }
}
