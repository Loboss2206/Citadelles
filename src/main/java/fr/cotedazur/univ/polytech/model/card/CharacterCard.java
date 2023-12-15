package fr.cotedazur.univ.polytech.model.card;

/**
 * Represents a role card in the game.
 */
public enum CharacterCard {
    ASSASSIN("Assassin", 1, Color.GRAY, "Tuez un personnage"),
    THIEF("Voleur", 2, Color.GRAY, "Volez l'argent d'un autre joueur"),
    MAGICIAN("Magicien", 3, Color.GRAY, "Echangez vos cartes avec celles d'un autre joueur"),
    KING("Roi", 4, Color.YELLOW, "Prenez 1 pièce d'or pour chaque quartier jaune que vous possédez"),
    BISHOP("Évêque", 5, Color.BLUE, "Prenez 1 pièce d'or pour chaque quartier bleu que vous possédez"),
    MERCHANT("Marchand", 6, Color.GREEN, "Prenez 1 pièce d'or pour chaque quartier vert que vous possédez"),
    ARCHITECT("Architecte", 7, Color.GRAY, "Piochez 2 cartes et possibilité de poser jusqu'à 3 bâtiments (si vous avez l'argent nécessaire)"),
    WARLORD("Condottiere", 8, Color.RED, "Détruisez un quartier en payant 1 pièce d'or de moins que son coût");

    private final String characterName;
    private final int characterNumber;
    private final Color color;
    private final String characterEffect;

    /**
     * Constructs a role card with the specified name, number, color, and description.
     *
     * @param characterName        the name of the character
     * @param characterNumber      the number of the character
     * @param color                the color of the character
     * @param characterEffect the description of the character's ability
     */
    private CharacterCard(String characterName, int characterNumber, Color color, String characterEffect) {
        this.characterName = characterName;
        this.characterNumber = characterNumber;
        this.color = color;
        this.characterEffect = characterEffect;
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
    public String getCharacterEffect() {
        return characterEffect;
    }
}
