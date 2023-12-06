package fr.cotedazur.univ.polytech.model.card;

/**
 * This enum represents the different district cards in the game.
 */
public enum DistrictCard {
    TAVERN(5, "Tavern", 1, "green"), MARKET(4, "Market", 2, "green"), TRADING_POST(3, "Trading Post", 2, "green"),
    DOCKS(3, "Docks", 3, "green"), HARBOR(3, "Harbor", 4, "green"), TOWN_HALL(2, "Town Hall", 5, "green"),
    TEMPLE(3, "Temple", 1, "blue"), CHURCH(3, "Church", 2, "blue"), MONASTERY(3, "Monastery", 3, "blue"),
    CATHEDRAL(2, "Cathedral", 5, "blue"), WATCHTOWER(3, "Watchtower", 1, "red"), PRISON(3, "Prison", 2, "red"),
    BATTLEFIELD(3, "Battlefield", 3, "red"), FORTRESS(2, "Fortress", 5, "red"), MANOR(5, "Manor", 3, "yellow"),
    CASTLE(4, "Castle", 4, "yellow"), PALACE(3, "Palace", 5, "yellow"), HAUNTED_CITY(1, "Haunted City", 2, "purple"),
    KEEP(2, "Keep", 3, "purple"), LABORATORY(1, "Laboratory", 5, "purple"), SMITHY(1, "Smithy", 5, "purple"),
    GRAVEYARD(1, "Graveyard", 5, "purple"), OBSERVATORY(1, "Observatory", 5, "purple"),
    SCHOOL_OF_MAGIC(1, "School of Magic", 6, "purple"), LIBRARY(1, "Library", 6, "purple"),
    GREAT_WALL(1, "Great Wall", 6, "purple"), UNIVERSITY(1, "University", 8, "purple"),
    DRAGON_GATE(1, "Dragon Gate", 8, "purple");

    /**
     * The quantity of this district card in the deck.
     */
    private final int quantityInDeck;
    private final String districtName;
    private final int districtValue;
    private final Color districtColor;

    /**
     * Constructs a DistrictCard with the specified attributes.
     *
     * @param quantityInDeck The quantity of this district card in the deck.
     * @param districtName   The name of the district card.
     * @param districtValue  The value of the district card.
     * @param districtColor  The color of the district card.
     */
    DistrictCard(int quantityInDeck, String districtName, int districtValue, String districtColor) {
        this.quantityInDeck = quantityInDeck;
        this.districtName = districtName;
        this.districtValue = districtValue;
        this.districtColor = Color.valueOf(districtColor.toUpperCase());
    }

    /**
     * Returns the name of the district card.
     *
     * @return The name of the district card.
     */
    public String getDistrictName() {
        return districtName;
    }

    /**
     * Returns the quantity of this district card in the deck.
     *
     * @return The quantity of this district card in the deck.
     */
    public int getQuantityInDeck() {
        return quantityInDeck;
    }

    /**
     * Returns the value of the district card.
     *
     * @return The value of the district card.
     */
    public int getDistrictValue() {
        return districtValue;
    }

    /**
     * Returns the color of the district card.
     *
     * @return The color of the district card.
     */
    public Color getDistrictColor() {
        return districtColor;
    }
}
