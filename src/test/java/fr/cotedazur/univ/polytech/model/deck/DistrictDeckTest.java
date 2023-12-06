package fr.cotedazur.univ.polytech.model.deck;

import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;

import static org.junit.jupiter.api.Assertions.*;

class DistrictDeckTest {

    @Test
    void shuffle() {
        Deck<DistrictCard> deck = DeckFactory.createDistrictDeck();
        deck.shuffle();
        assertNotEquals(deck, DeckFactory.createDistrictDeck());
    }
}