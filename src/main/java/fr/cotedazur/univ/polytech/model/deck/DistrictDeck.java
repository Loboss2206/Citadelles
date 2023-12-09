package fr.cotedazur.univ.polytech.model.deck;

import java.util.List;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;

/**
 * Represents a deck of district cards.
 * Extends the generic Deck class with DistrictCard type.
 */
public class DistrictDeck extends Deck<DistrictCard> {
    /**
     * Constructs a DistrictDeck object with the given list of cards.
     *
     * @param cards The list of district cards to initialize the deck with.
     */
    public DistrictDeck(List<DistrictCard> cards) {
        super(cards);
    }

    /**
     * Constructs an empty DistrictDeck object.
     */
    public DistrictDeck() {
        super();
    }

    /**
     * Shuffles the cards in the deck using the Fisher-Yates algorithm.
     */
    @Override
    public void shuffle() {
        for (int i = getCards().size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            DistrictCard temp = getCards().get(i);
            getCards().set(i, getCards().get(j));
            getCards().set(j, temp);
        }
    }
}
