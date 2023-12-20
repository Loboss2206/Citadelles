package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final int id;
    private final String name;
    private int golds;
    private final ArrayList<DistrictCard> hands;
    private CharacterCard playerRole;
    private final ArrayList<DistrictCard> board;//This is for when a player choose to put a district
    private int points;
    protected ArrayList<DistrictCard> validCards;

    // Increment for each player created
    private static int count = 0;

    protected Player() {
        id = count++;
        this.name = "BOT" + id;
        this.golds = 0;
        this.hands = new ArrayList<>();
        this.playerRole = null;
        this.board = new ArrayList<>();
        this.validCards = new ArrayList<>();
    }

    public int getGolds() {
        return golds;
    }

    public void removeGold(int golds) {
        this.golds -= golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public List<DistrictCard> getHands() {
        return hands;
    }

    public CharacterCard getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(CharacterCard playerRole) {
        this.playerRole = playerRole;
    }

    public List<DistrictCard> getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public static void setCount(int count) {
        Player.count = count;
    }

    /**
     * function that draw a card from the district deck if its possible, else the player take 2 golds
     * @param districtDeck the district deck
     * @return the name of the card drawn
     */
    public String drawCard(DistrictDeck districtDeck) {
        if(districtDeck.isEmpty()) {
            return collectTwoGolds();
        }else {
            hands.add(districtDeck.draw());
            return "drawCard";
        }
    }

    /**
     * function that take 2 golds, if the player has chosen this option instead of draw a card
     */
    public String collectTwoGolds() {
        this.golds += 2;
        return "2golds";
    }

    /**
     * add a card to the board
     * @param cardName the name of the card to add
     */
    public void addCardToBoard(String cardName) {
        for (DistrictCard card : hands) {
            if (card.name().equals(cardName)) {
                board.add(card);
                hands.remove(card);
                break;
            }
        }
    }

    /**
     * Function that check all the cards in the hand of the player and add the cards that are buy-able by the player to the list validCards
     */
    public void discoverValidCard() {
        validCards.clear();
        for (DistrictCard card : getHands()) {
            if (card.getDistrictValue() <= getGolds()) {
                validCards.add(card);
            }
        }
    }

    /**
     * check if a card is on the board of a player
     * @param cardName the name of the card to check
     * @return true if the card is on the board, else false
     */
    public boolean hasCardOnTheBoard(String cardName) {
        for (DistrictCard card : board) {
            if (card.name().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the player has a playable card
     * @return true if the player has a playable card, else false
     */
    public boolean hasPlayableCard() {
        for (DistrictCard card : hands) {
            if (!hasCardOnTheBoard(card.name()) && validCards.contains(card)) {
                return true;
            }
        }
        return false;
    }

    /**
     * function where the player choose between take 2 golds or draw a card
     * @param districtDeck the district deck
     * @return his choice
     */
    public abstract String startChoice(DistrictDeck districtDeck);

    /**
     * function where the player choose to put or not a district on his board
     * @return his choice or null if he doesn't want to put a district
     */
    public abstract String choiceToPutADistrict();

    /**
     * function where the player choose his character
     * @param cards the characters available
     * @return the index of the character chosen
     */
    public abstract int chooseCharacter(List<CharacterCard> cards);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player player) {
            return player.id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
