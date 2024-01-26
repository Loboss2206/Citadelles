package fr.cotedazur.univ.polytech.model.deck;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DistrictDeckTest {

    @Test
    void shuffle() {
        Deck<DistrictCard> deck = DeckFactory.createDistrictDeck();
        deck.shuffle();
        assertNotEquals(deck, DeckFactory.createDistrictDeck());
    }
}