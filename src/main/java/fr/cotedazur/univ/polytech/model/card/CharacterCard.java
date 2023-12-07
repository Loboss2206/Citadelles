package fr.cotedazur.univ.polytech.model.card;

/**
 * Represents a role card in the game.
 */
public enum CharacterCard {
    ASSASSIN("Assassin", 1, Color.GRAY, "Kill a character"),
    THIEF("Thief", 2, Color.GRAY, "Steal gold from a character"),
    MAGICIAN("Magician", 3, Color.GRAY, "Exchange cards with another character"),
    KING("King", 4, Color.YELLOW, "Take 1 gold for each noble district you own"),
    BISHOP("Bishop", 5, Color.BLUE, "Take 1 gold for each religious district you own"),
    MERCHANT("Merchant", 6, Color.GREEN, "Take 1 gold for each trade district you own"),
    ARCHITECT("Architect", 7, Color.GRAY, "Draw 2 cards, then put 2 cards"),
    WARLORD("Warlord", 8, Color.RED, "Destroy a district, pay 1 gold less than its cost");

    private final String characterName;
    private final int characterNumber;
    private final Color color;
    private final String characterDescription;

    /**
     * Constructs a role card with the specified name, number, color, and description.
     *
     * @param characterName        the name of the character
     * @param characterNumber      the number of the character
     * @param color                the color of the character
     * @param characterDescription the description of the character's ability
     */
    private CharacterCard(String characterName, int characterNumber, Color color, String characterDescription) {
        this.characterName = characterName;
        this.characterNumber = characterNumber;
        this.color = color;
        this.characterDescription = characterDescription;
    }

    /**
     * Returns the name of the character.
     *
     * @return the name of the character
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Returns the number of the character.
     *
     * @return the number of the character
     */
    public int getCharacterNumber() {
        return characterNumber;
    }

    /**
     * Returns the color of the character.
     *
     * @return the color of the character
     */
    public Color getCharacterColor() {
        return color;
    }

    /**
     * Returns the description of the character's ability.
     *
     * @return the description of the character's ability
     */
    public String getCharacterDescription() {
        return characterDescription;
    }
}
