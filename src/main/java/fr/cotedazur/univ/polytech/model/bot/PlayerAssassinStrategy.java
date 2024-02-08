package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

import java.util.*;

public abstract class PlayerAssassinStrategy extends Player {
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.ASSASSIN) {
            LOGGER.info("Le joueur " + getName() + " utilise l'effet de l'assassin");
            if (players.size() < 4) return characterCards.get(3);
            if (players.size() < 6) return characterCards.get(5);
            else return characterCards.get(6);
        }
        LOGGER.info("Le joueur " + getName() + " ne peut pas utiliser l'effet de l'assassin");
        return null;
    }

    /**
     * function that checks whether there is at least one color on the player's board that can be improved by a character
     */
    protected boolean hasColoredCards() {
        return countNumberOfSpecifiedColorCard(Color.YELLOW) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.GREEN) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.BLUE) >= 1;
    }

    /**
     * function that count the number of district card on the board for a specific color
     */
    protected int countNumberOfSpecifiedColorCard(Color color) {
        int count = 0;
        for (DistrictCard card : getBoard()) {
            if (card.getDistrictColor() == color) count++;
        }
        return count;
    }

    /**
     * Creates a HashMap that maps each specified character card to its corresponding color count.
     *
     * @param characters the characters available
     * @return {@code Map<Color, Integer>} where the keys are colors associated with the specified character cards
     * and the values are the counts of cards of that color in the given list.
     */
    protected Map<Color, Integer> createColorMap(List<CharacterCard> characters) {
        Map<Color, Integer> hashMap = new EnumMap<>(Color.class);
        if (characters.contains(CharacterCard.KING))
            hashMap.put(Color.YELLOW, countNumberOfSpecifiedColorCard(Color.YELLOW));
        if (characters.contains(CharacterCard.BISHOP))
            hashMap.put(Color.BLUE, countNumberOfSpecifiedColorCard(Color.BLUE));
        if (characters.contains(CharacterCard.MERCHANT))
            hashMap.put(Color.GREEN, countNumberOfSpecifiedColorCard(Color.GREEN));
        return hashMap;
    }

    protected int getCharacterAccordingToColor(List<CharacterCard> characters) {
        Map<Color, Integer> colorMap = createColorMap(characters);
        List<Map.Entry<Color, Integer>> entryList = new ArrayList<>(colorMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        if (!entryList.isEmpty()) {
            LOGGER.info("Le joueur " + getName() + " prend le personnage " + entryList.get(0).getKey() + " car il a le plus de quartiers de cette couleur");
            return getCharacterIndexByColor(characters, entryList.get(0).getKey());
        }
        return -1;
    }

    /**
     * Retrieves the index of a specific character card in the given list based on its associated color.
     *
     * @param characters the characters available
     * @param color      The color associated with the character card to find.
     * @return The index of the character card associated with the specified color, or an exception if not found.
     */
    int getCharacterIndexByColor(List<CharacterCard> characters, Color color) {
        return switch (color) {
            case YELLOW -> characters.indexOf(CharacterCard.KING);
            case GREEN -> characters.indexOf(CharacterCard.MERCHANT);
            case BLUE -> characters.indexOf(CharacterCard.BISHOP);
            default -> throw new UnsupportedOperationException("la valeur de color est : " + color);
        };
    }

}
