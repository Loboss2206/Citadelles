package fr.cotedazur.univ.polytech.model.card;

/**
 * This enum represents the different district cards in the game.
 */
public enum DistrictCard {
    TAVERN(5, "Taverne", 1, "green"), MARKET(4, "Marché", 2, "green"), TRADING_POST(3, "Comptoir commercial", 2, "green"),
    DOCKS(3, "Quais", 3, "green"), HARBOR(3, "Port", 4, "green"), TOWN_HALL(2, "Mairie", 5, "green"),
    TEMPLE(3, "Temple", 1, "blue"), CHURCH(3, "Église", 2, "blue"), MONASTERY(3, "Monastère", 3, "blue"),
    CATHEDRAL(2, "Cathédrale", 5, "blue"), WATCHTOWER(3, "Tour de guet", 1, "red"), PRISON(3, "Prison", 2, "red"),
    BATTLEFIELD(3, "Champ de bataille", 3, "red"), FORTRESS(2, "Forteresse", 5, "red"), MANOR(5, "Manoir", 3, "yellow"),
    CASTLE(4, "Château", 4, "yellow"), PALACE(3, "Palais", 5, "yellow"), HAUNTED_CITY(1, "Cour des miracles", 2, "purple"),
    KEEP(2, "Donjon", 3, "purple"), LABORATORY(1, "Laboratoire", 5, "purple"), SMITHY(1, "Manufacture", 5, "purple"),
    GRAVEYARD(1, "Cimetière", 5, "purple"), OBSERVATORY(1, "Observatoire", 5, "purple"), SCHOOL_OF_MAGIC(1, "École de Magie", 6, "purple"),
    LIBRARY(1, "Bibliothèque", 6, "purple"), UNIVERSITY(1, "Université", 6, "purple"), DRAGON_GATE(1, "Dracoport", 6, "purple");

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

    /**
     * Returns true if the district card can be destroyed by the warlord
     * @param golds The number of golds of the warlord
     * @return true if the district card can be destroyed by the warlord, else false
     */
    public boolean isDestroyableDistrict(int golds) {
        if (golds >= getDistrictValue() - 1) {
            return !getDistrictName().equals("Donjon");
        }
        else return false;
    }
}
