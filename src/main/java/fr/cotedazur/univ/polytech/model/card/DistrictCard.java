package fr.cotedazur.univ.polytech.model.card;

/**
 * This enum represents the different district cards in the game.
 */
public enum DistrictCard {
    TAVERN(5, "Taverne", 1, Color.GREEN), MARKET(4, "Marché", 2, Color.GREEN), TRADING_POST(3, "Comptoir commercial", 2, Color.GREEN),
    DOCKS(3, "Quais", 3, Color.GREEN), HARBOR(3, "Port", 4, Color.GREEN), TOWN_HALL(2, "Mairie", 5, Color.GREEN),
    TEMPLE(3, "Temple", 1, Color.BLUE), CHURCH(3, "Église", 2, Color.BLUE), MONASTERY(3, "Monastère", 3, Color.BLUE),
    CATHEDRAL(2, "Cathédrale", 5, Color.BLUE), WATCHTOWER(3, "Tour de guet", 1, Color.RED), PRISON(3, "Prison", 2, Color.RED),
    BATTLEFIELD(3, "Champ de bataille", 3, Color.RED), FORTRESS(2, "Forteresse", 5, Color.RED), MANOR(5, "Manoir", 3, Color.YELLOW),
    CASTLE(4, "Château", 4, Color.YELLOW), PALACE(3, "Palais", 5, Color.YELLOW), HAUNTED_CITY(1, "Cour des miracles", 2, Color.PURPLE),
    KEEP(2, "Donjon", 3, Color.PURPLE), LABORATORY(1, "Laboratoire", 5, Color.PURPLE), SMITHY(1, "Manufacture", 5, Color.PURPLE),
    GRAVEYARD(1, "Cimetière", 5, Color.PURPLE), OBSERVATORY(1, "Observatoire", 5, Color.PURPLE), SCHOOL_OF_MAGIC(1, "École de Magie", 6, Color.PURPLE),
    LIBRARY(1, "Bibliothèque", 6, Color.PURPLE), UNIVERSITY(1, "Université", 6, Color.PURPLE), DRAGON_GATE(1, "Dracoport", 6, Color.PURPLE);

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
    DistrictCard(int quantityInDeck, String districtName, int districtValue, Color districtColor) {
        this.quantityInDeck = quantityInDeck;
        this.districtName = districtName;
        this.districtValue = districtValue;
        this.districtColor = districtColor;
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
     *
     * @param golds The number of golds of the warlord
     * @return true if the district card can be destroyed by the warlord, else false
     */
    public boolean isDestroyableDistrict(int golds) {
        if (golds >= getDistrictValue() - 1) {
            return !getDistrictName().equals("Donjon");
        } else return false;
    }
}
