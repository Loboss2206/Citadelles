package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements GameActions {
    private final int id;
    private final String name;
    private int golds;
    private final ArrayList<DistrictCard> hands;
    private CharacterCard playerRole;
    private final ArrayList<DistrictCard> board;//This is for when a player choose to put a district
    private int points;
    private boolean isCrowned = false;
    protected ArrayList<DistrictCard> validCards;

    boolean isFirstToAdd8district = false;

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
     * @param card the card to add
     */
    public void addCardToBoard(DistrictCard card) {
        board.add(card);
        hands.remove(card);
        removeGold(card.getDistrictValue());
    }

    /**
     * Function that check all the cards in the hand of the player and add the cards that are buy-able by the player to the list validCards
     */
    public void discoverValidCard() {
        validCards.clear();
        for (DistrictCard card : getHands()) {
            if (card.getDistrictValue() <= getGolds() && !hasCardOnTheBoard(card)) {
                validCards.add(card);
            }
        }
    }

    /**
     * check if a card is on the board of a player
     * @param card the card to check
     * @return true if the card is on the board, else false
     */
    public boolean hasCardOnTheBoard(DistrictCard card) {
        if (board.isEmpty() || card == null) return false;
        for (DistrictCard c : board) {
            if (c.name().equals(card.name())) {
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
            if (!hasCardOnTheBoard(card) && validCards.contains(card)) {
                return true;
            }
        }
        return false;
    }

    public void setCrowned(boolean isCrowned) {
        this.isCrowned = isCrowned;
    }

    public boolean isCrowned() {
        return isCrowned;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player player) {
            return player.id == this.id;
        }
        return false;
    }

    public void drawAndPlaceADistrict(GameView view) {
        DistrictCard districtToPut;
        do {
            districtToPut = choiceToPutADistrict();
        } while (hasCardOnTheBoard(districtToPut) && hasPlayableCard());
        if (districtToPut != null && !hasCardOnTheBoard(districtToPut)) {
            addCardToBoard(districtToPut);
            if (view != null) view.printPlayerAction("putDistrict", this);
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public void setFirstToAdd8district(boolean firstToAdd8district) {
        isFirstToAdd8district = firstToAdd8district;
    }

    public boolean isFirstToAdd8district() {
        return isFirstToAdd8district;
    }
}
