package fr.cotedazur.univ.polytech.model.deck;

import java.util.List;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;

/**
 * Represents a deck of character cards in the game.
 * Extends the generic Deck class with RoleCard as the card type.
 */
public class CharacterDeck extends Deck<CharacterCard> {

    /**
     * Constructs a CharacterDeck with the given list of cards.
     * 
     * @param cards The list of RoleCard objects to be included in the deck.
     */
    public CharacterDeck(List<CharacterCard> cards) {
        super(cards);
    }

    /**
     * Constructs an empty CharacterDeck.
     */
    public CharacterDeck() {
        super();
    }

    /**
     * Shuffles the cards in the deck using the Fisher-Yates algorithm.
     */
    @Override
    public void shuffle() {
        for (int i = getCards().size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            CharacterCard temp = getCards().get(i);
            getCards().set(i, getCards().get(j));
            getCards().set(j, temp);
        }
    }
}
