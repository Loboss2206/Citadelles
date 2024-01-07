package fr.cotedazur.univ.polytech.model.card;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

/**
 * Represents a role card in the game.
 */
public enum CharacterCard {
    ASSASSIN("Assassin", 1, Color.GRAY, "Tuez un personnage"),
    THIEF("Voleur", 2, Color.GRAY, "Volez l'argent d'un autre joueur"),
    MAGICIAN("Magicien", 3, Color.GRAY, "Echangez vos cartes avec celles d'un autre joueur"),
    KING("Roi", 4, Color.YELLOW, "Prenez 1 pièce d'or pour chaque quartier jaune que vous possédez"),
    BISHOP("Évêque", 5, Color.BLUE, "Prenez 1 pièce d'or pour chaque quartier bleu que vous possédez. Les quartiers de l'Évêque ne peuvent pas être détruits par le Condottiere."),
    MERCHANT("Marchand", 6, Color.GREEN, "Prenez 1 pièce d'or pour chaque quartier vert dans votre quartier et une pièce d'or supplémentaire après une action"),
    ARCHITECT("Architecte", 7, Color.GRAY, "Piochez 2 cartes et possibilité de poser jusqu'à 3 bâtiments (si vous avez l'argent nécessaire)"),
    WARLORD("Condottiere", 8, Color.RED, "Détruisez un quartier en payant 1 pièce d'or de moins que son coût");

    private final String characterName;
    private final int characterNumber;
    private final Color color;
    private final String characterEffect;

    /**
     * Constructs a role card with the specified name, number, color, and description.
     *
     * @param characterName   the name of the character
     * @param characterNumber the number of the character
     * @param color           the color of the character
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

    /**
     * Use the effect of the character card.
     *
     * @param player the player who use the effect
     */
    public void useEffect(Player player) {
        switch (this) {
            case ASSASSIN -> {
                //TODO TO TEST
            }
            case THIEF -> {
                //TODO TO TEST
            }
            case MAGICIAN -> {
                //TODO TO TEST
            }
            case KING -> {
                earnGoldsFromDistricts(player, Color.YELLOW);
                System.out.println(player.getName()+" utilise l'effet du roi");
            }
            case BISHOP -> {
                earnGoldsFromDistricts(player, Color.BLUE);
                System.out.println(player.getName()+" utilise l'effet de l'évêque");
            }
            case MERCHANT -> {
                earnGoldsFromDistricts(player, Color.GREEN);
                player.setGolds(player.getGolds() + 1);
                System.out.println(player.getName()+" utilise l'effet du marchant");
            }
            case WARLORD -> {
                //TODO TO TEST
            }
        }
    }

    /**
     * Use the effect of the character card.
     *
     * @param player the player who use the effect
     */
    public void useEffect(Player player, DistrictDeck districtDeck) {
        if (player.getPlayerRole() == CharacterCard.ARCHITECT) {
            for (int i = 0; i < 2; i++) {
                player.drawCard(districtDeck);
            }
        }
        System.out.println("Pioche 2 cartes grâce à l'effet de l'architecte");
    }

    /**
     * Use the effect of the character card.
     *
     * @param player the player who use the effect
     */
    public void useEffect(Player player, GameView view) {
        if (player.getPlayerRole() == CharacterCard.ARCHITECT) {
            for (int i = 0; i < 2; i++) {
                player.drawAndPlaceADistrict(view);
            }
        }
        System.out.println("Pose une carte supplémentaire grâce à l'effet de l'architecte");
    }


    /**
     * Earn golds from the districts of the specified color.
     *
     * @param player the player who earn golds
     * @param color  the color of the districts
     */
    public void earnGoldsFromDistricts(Player player, Color color) {
        for (DistrictCard district : player.getBoard()) {
            if (district.getDistrictColor() == color) {
                player.setGolds(player.getGolds() + 1);
            }
        }
    }
}
