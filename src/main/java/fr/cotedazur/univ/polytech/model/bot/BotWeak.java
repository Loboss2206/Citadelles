package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;

public class BotWeak extends Player implements GameActions {

    public BotWeak() {
        super();
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        if (!validCards.isEmpty()) {
            //Sort the hands from the smallest to the biggest
            validCards.sort(new DistrictCardComparator());
            return validCards.get(0);
        }
        return null;
    }

    @Override
    public String startChoice(Deck<DistrictCard> districtDeck) {
        discoverValidCard();
        if (getHands().isEmpty() || !validCards.isEmpty()) {
            return drawCard(districtDeck);
        }
        return collectTwoGolds();
    }

    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        return putADistrict();
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players,  List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.THIEF) {
            return characterCards.get(4);
        }
        return null;
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        discoverValidCard();
        // we sort to know if we can put 2 times a district or more by comparing the first two value
        validCards.sort(new DistrictCardComparator());

        if (isArchitectOptimal(characters)) {
            return characters.indexOf(CharacterCard.ARCHITECT);
        } else if (hasColoredCards()) {
            HashMap<Color, Integer> colorMap = createColorMap(characters);
            List<Map.Entry<Color, Integer>> entryList = new ArrayList<>(colorMap.entrySet());
            entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

            if (!entryList.isEmpty()) {
                return getCharacterIndexByColor(characters, entryList.get(0).getKey());
            }

        }

        Random random = new Random();
        return random.nextInt(characters.size());
    }

    /**
     * function that checks whether it's worth taking the architect
     *
     * @param characters the characters available
     * @return true if it's worth taking the architect, else false
     */
    private boolean isArchitectOptimal(List<CharacterCard> characters) {
        return (validCards.size() >= 2 && characters.contains(CharacterCard.ARCHITECT) &&
                validCards.get(0).getDistrictValue() + validCards.get(1).getDistrictValue() <= getGolds()) ||
                (getHands().isEmpty() && characters.contains(CharacterCard.ARCHITECT));
    }

    /**
     * function that checks whether there is at least one color on the player's board that can be improved by a character
     */
    private boolean hasColoredCards() {
        return countNumberOfSpecifiedColorCard(Color.YELLOW) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.GREEN) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.BLUE) >= 1;
    }

    /**
     * function that count the number of district card on the board for a specific color
     */
    public int countNumberOfSpecifiedColorCard(Color color) {
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
     * @return A HashMap<Color, Integer> where the keys are colors associated with the specified character cards
     * and the values are the counts of cards of that color in the given list.
     */
    private HashMap<Color, Integer> createColorMap(List<CharacterCard> characters) {
        HashMap<Color, Integer> hashMap = new HashMap<>();
        if (characters.contains(CharacterCard.KING))
            hashMap.put(Color.YELLOW, countNumberOfSpecifiedColorCard(Color.YELLOW));
        if (characters.contains(CharacterCard.BISHOP))
            hashMap.put(Color.BLUE, countNumberOfSpecifiedColorCard(Color.BLUE));
        if (characters.contains(CharacterCard.MERCHANT))
            hashMap.put(Color.GREEN, countNumberOfSpecifiedColorCard(Color.GREEN));
        return hashMap;
    }

    /**
     * Retrieves the index of a specific character card in the given list based on its associated color.
     *
     * @param characters the characters available
     * @param color      The color associated with the character card to find.
     * @return The index of the character card associated with the specified color, or an exception if not found.
     */
    private int getCharacterIndexByColor(List<CharacterCard> characters, Color color) {
        return switch (color) {
            case YELLOW -> characters.indexOf(CharacterCard.KING);
            case GREEN -> characters.indexOf(CharacterCard.MERCHANT);
            case BLUE -> characters.indexOf(CharacterCard.BISHOP);
            default -> throw new UnsupportedOperationException("la valeur de color est : " + color);
        };
    }

    public Player copyPlayer() {
        return new BotWeak();
    }

    @Override
    public String WhichWarlordEffect() {
        return null;
    }
}
