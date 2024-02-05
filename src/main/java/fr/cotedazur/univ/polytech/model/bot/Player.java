package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Player implements GameActions {
    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());
    // Increment for each player created
    private static int count = 0;
    //All players have a unique id
    private final int id;
    //cards in the hand of the player which he can buy during his turn
    protected ArrayList<DistrictCard> validCards;
    //to find out if the player is the first to add 8 district on his board
    boolean isFirstToAdd8district = false;
    //All players have a unique name
    private String name;
    //The amount of gold for a player
    private int golds;
    //Districts in the player's hand
    private List<DistrictCard> hands;
    //the player's role
    private CharacterCard playerRole;
    //Districts on the player's board
    private List<DistrictCard> board;
    //the player's number of points
    private int points;
    //the player's current status
    private boolean isDead = false;
    private boolean hasBeenStolen = false;
    //to find out if the player is the king
    private boolean isCrowned = false;
    //The character effect that the player has used during his turn
    private String usedEffect;
    private int nbCardsInHand = 0;
    private int whatIsTheRoundWhereThePlayerPutHisHauntedCity = 0;

    protected Player() {
        id = count++;
        this.name = "BOT" + id;
        this.golds = 0;
        this.hands = new ArrayList<>();
        this.playerRole = null;
        this.board = new ArrayList<>();
        this.validCards = new ArrayList<>();
    }

    public static void setCount(int count) {
        Player.count = count;
    }

    public int getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
        LOGGER.info("Le joueur " + name + " a maintenant " + golds + " pièces d'or");
    }

    public void removeGold(int golds) {
        this.golds -= golds;
        LOGGER.info("Le joueur " + name + " a perdu " + golds + " pièces d'or, il lui reste " + this.golds + " pièces d'or");
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

    public void setHands(List<DistrictCard> hands) {
        this.hands = hands;
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

    public void setBoard(List<DistrictCard> board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * function that take 2 golds, if the player has chosen this option instead of draw a card
     */
    public String collectTwoGolds() {
        this.golds += 2;
        LOGGER.info("Le joueur " + name + " a pioché 2 pièces d'or, il a maintenant " + golds + " pièces d'or");
        return DispatchState.TWO_GOLDS.toString();
    }

    /**
     * add a card to the board
     *
     * @param card the card to add
     */
    public void addCardToBoard(DistrictCard card) {
        board.add(card);
        hands.remove(card);
        nbCardsInHand--;
        LOGGER.info("Le joueur " + name + " a posé le quartier " + card.getDistrictName() + "(" + card.getDistrictValue() + " pièces d'or) (couleur " + card.getDistrictColor() + ")");
        LOGGER.info(" sur son plateau, il a maintenant " + board.size() + " quartiers sur son plateau, il lui reste " + nbCardsInHand + " cartes en main");
    }

    /**
     * add a card to the hand
     *
     * @param card the card to add
     */
    public void addCardToHand(DistrictCard card) {
        hands.add(card);
        nbCardsInHand++;
        LOGGER.info("Le joueur " + name + " a pioché le quartier " + card.getDistrictName() + "(" + card.getDistrictValue() + " pièces d'or) (couleur " + card.getDistrictColor() + ")");
        LOGGER.info(" il a maintenant " + board.size() + " quartiers sur son plateau, il lui reste " + nbCardsInHand + " cartes en main");
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
        LOGGER.info("Le joueur " + name + " a " + validCards.size() + " cartes achetables dont " + validCards);
    }

    /**
     * check if a card is on the board of a player
     *
     * @param card the card to check
     * @return true if the card is on the board, else false
     */
    public boolean hasCardOnTheBoard(DistrictCard card) {
        if (board.isEmpty() || card == null) return false;
        for (DistrictCard c : board) {
            if (c.name().equals(card.name())) {
                LOGGER.info("Le joueur " + name + " a déjà le quartier " + card.getDistrictName() + " sur son plateau");
                return true;
            }
        }
        LOGGER.info("Le joueur " + name + " n'a pas le quartier " + card.getDistrictName() + " sur son plateau");
        return false;
    }

    /**
     * check if the player has a playable card
     *
     * @return true if the player has a playable card, else false
     */
    public boolean hasPlayableCard() {
        for (DistrictCard card : hands) {
            if (!hasCardOnTheBoard(card) && validCards.contains(card)) {
                LOGGER.info("Le joueur " + name + " a une carte achetable");
                return true;
            }
        }
        LOGGER.info("Le joueur " + name + " n'a pas de carte achetable");
        return false;
    }

    public boolean isCrowned() {
        return isCrowned;
    }

    public void setCrowned(boolean isCrowned) {
        LOGGER.info("Le joueur " + name + (isCrowned ? " est" : " n'est plus") + " le roi");
        this.isCrowned = isCrowned;
    }

    /**
     * function that check if 2 object are equals
     *
     * @return true if the obj is equals to this, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        return Objects.equals(name, ((Player) o).name);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isFirstToAdd8district() {
        return isFirstToAdd8district;
    }

    public void setFirstToAdd8district(boolean firstToAdd8district) {
        LOGGER.info("Le joueur " + name + (firstToAdd8district ? " est" : " n'est plus") + " le premier à avoir 8 quartiers sur son plateau");
        isFirstToAdd8district = firstToAdd8district;
    }

    /**
     * function that copy a player without his hand
     *
     * @return the copy of the player
     */
    public Player copy() {
        Player copy = new BotRandom();

        copy.setName(this.getName());
        copy.setBoard(this.getBoard());
        copy.setGolds(this.getGolds());
        copy.setNbCardsInHand(this.getNbCardsInHand());
        copy.setCrowned(this.isCrowned());
        return copy;
    }

    /**
     * Check if a player has a district that can be destroyed
     *
     * @param warlord the warlord
     * @return true if the player has a district that can be destroyed, else false
     */
    public boolean playerHasADestroyableDistrict(Player warlord) {
        if (this.getBoard().isEmpty() || this.getBoard().size() >= 8 || (this.getPlayerRole().equals(CharacterCard.BISHOP) && !this.isDead())) {
            LOGGER.info("Le joueur " + name + " n'a pas de quartier détruisable");
            return false;
        }
        for (DistrictCard district : this.getBoard()) {
            if (district.isDestroyableDistrict(warlord.getGolds())) {
                LOGGER.info("Le joueur " + name + " a un quartier détruisable");
                return true;
            }
        }
        LOGGER.info("Le joueur " + name + " n'a pas de quartier détruisable");
        return false;
    }

    public int getNbCardsInHand() {
        return nbCardsInHand;
    }

    public void setNbCardsInHand(int nbCardsInHand) {
        this.nbCardsInHand = nbCardsInHand;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        LOGGER.info("Le joueur " + name + (isDead ? " est" : " n'est plus") + " mort");
        this.isDead = isDead;
    }

    public boolean isStolen() {
        return hasBeenStolen;
    }

    public void setHasBeenStolen(boolean hasBeenStolen) {
        this.hasBeenStolen = hasBeenStolen;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getWhatIsTheRoundWhereThePlayerPutHisHauntedCity() {
        return whatIsTheRoundWhereThePlayerPutHisHauntedCity;
    }

    public void setWhatIsTheRoundWhereThePlayerPutHisHauntedCity(int whatIsTheRoundWhereThePlayerPutHisHauntedCity) {
        this.whatIsTheRoundWhereThePlayerPutHisHauntedCity = whatIsTheRoundWhereThePlayerPutHisHauntedCity;
    }

    public abstract DistrictCard chooseHandCardToDiscard();
}


