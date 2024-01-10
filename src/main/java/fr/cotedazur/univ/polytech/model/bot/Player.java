package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.ArrayList;
import java.util.List;

public abstract class Player implements GameActions {
    //All players have a unique id
    private final int id;

    //All players have a unique name
    private final String name;

    //The amount of gold for a player
    private int golds;

    //Districts in the player's hand
    private final ArrayList<DistrictCard> hands;

    //the player's role
    private CharacterCard playerRole;
    //Districts on the player's board
    private final ArrayList<DistrictCard> board;

    //the player's number of points
    private int points;
    //to find out if the player is the king
    private boolean isCrowned = false;

    //cards in the hand of the player which he can buy during his turn
    protected ArrayList<DistrictCard> validCards;

    //The character effect that the player has used during his turn
    private String usedEffect;

    //to find out if the player is the first to add 8 district on his board
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

    public String getUsedEffect() {
        return usedEffect;
    }

    public void setUsedEffect(String hasUsedEffect) {
        this.usedEffect = hasUsedEffect;
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
    public String drawCard(Deck<DistrictCard> districtDeck) {
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

    /**
     * function that check if 2 object are equals
     * @return true if the obj is equals to this, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player player) {
            return player.id == this.id;
        }
        return false;
    }

    /**
     * function that add to the board the district he chose to put and announced it
     */
    public void drawAndPlaceADistrict(GameView view) {
        DistrictCard districtToPut;
        do {
            districtToPut = choiceHowToPlayDuringTheRound();
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


