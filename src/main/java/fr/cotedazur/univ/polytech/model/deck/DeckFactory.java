package fr.cotedazur.univ.polytech.model.deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.cotedazur.univ.polytech.model.card.RoleCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

/**
 * The DeckFactory class is responsible for creating different types of decks.
 */
public class DeckFactory {
    
    /**
     * Creates a character deck containing all the role cards.
     * 
     * @return The character deck.
     */
    public static Deck<RoleCard> createCharacterDeck() {
        List<RoleCard> cards = new ArrayList<>(Arrays.asList(RoleCard.values()));
        return new CharacterDeck(cards);
    }

    /**
     * Creates a district deck containing all the dist rict cards.
     * 
     * @return The district deck.
     */
    public static Deck<DistrictCard> createDistrictDeck() {
        List<DistrictCard> cards = new ArrayList<>();
        for (DistrictCard card : DistrictCard.values()) {
            for (int i = 0; i < card.getQuantityInDeck(); i++) {
                cards.add(card);
            }
        }
        return new DistrictDeck(cards);
    }
    
    /**
     * Creates an empty district deck.
     * 
     * @return The empty district deck.
     */
    public static Deck<DistrictCard> createEmptyDistrictDeck() {
        return new DistrictDeck();
    }

    /**
     * Creates an empty character deck.
     * 
     * @return The empty character deck.
     */
    public static Deck<RoleCard> createEmptyCharacterDeck() {
        return new CharacterDeck();
    }

}
