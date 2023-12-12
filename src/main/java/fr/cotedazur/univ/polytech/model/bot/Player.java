package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private int id = 0;
    private final String name;
    private int golds;
    private final ArrayList<DistrictCard> hands;
    private CharacterCard playerRole;
    private final ArrayList<DistrictCard> board;//This is for when a player choose to put a district
    private int points;

    // Increment for each player created
    private static int count = 0;

    protected Player() {
        id = count++;
        this.name = "BOT" + id;
        this.golds = 0;
        this.hands = new ArrayList<>();
        this.playerRole = null;
        this.board = new ArrayList<>();
    }


    public int getGolds() {
        return golds;
    }

    public List<DistrictCard> getHands() {
        return hands;
    }

    public CharacterCard getPlayerRole() {
        return playerRole;
    }

    public void setGolds(int golds) {
        this.golds = golds;
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

    public void drawCard(DistrictDeck districtDeck) {
        hands.add(districtDeck.draw());
    }

    /**
     * function that take 2 golds, if the player has chosen this option instead of draw a card
     */
    public void collectTwoGolds() {
        this.golds += 2;
    }

    //These functions are abstract because it depends on ont the bot type
    public abstract boolean putADistrict();

    public abstract String startChoice(DistrictDeck districtDeck);

    public abstract String choiceToPutADistrict();

    public abstract void useRoleEffect();

    /**
     * The function take a list of character cards and return the number of the character card chosen by the player (the number is the index of the list)
     * @param cards the list of character cards
     * @return the number of the character card chosen by the player (the number is the index of the list)
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
