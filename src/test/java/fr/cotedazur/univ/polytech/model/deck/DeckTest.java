package fr.cotedazur.univ.polytech.model.deck;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class DeckTest {

    // Test case for an empty deck
    @Test
    void testEmptyDeck() {
        DistrictDeck emptyDeck = new DistrictDeck();
        assertTrue(emptyDeck.isEmpty());
        assertEquals(0, emptyDeck.size());
    }

    // Test case for adding and removing a card
    @Test
    void testAddAndRemoveCard() {
        DistrictDeck deck = new DistrictDeck();
        DistrictCard card = DistrictCard.CASTLE;

        deck.add(card);
        assertFalse(deck.isEmpty());
        assertEquals(1, deck.size());
        assertTrue(deck.contains(card));

        DistrictCard drawnCard = deck.draw();
        assertEquals(card, drawnCard);
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    // Test case for drawing a card at a specific index
    @Test
    void testDrawAtIndex() {
        DistrictDeck deck = new DistrictDeck();
        DistrictCard card1 = DistrictCard.DOCKS;
        DistrictCard card2 = DistrictCard.CHURCH;

        deck.add(card1);
        deck.add(card2);

        DistrictCard drawnCard = deck.draw(1);
        assertEquals(card2, drawnCard);
        assertFalse(deck.contains(card2));
        assertEquals(1, deck.size());
    }

    // Test case for shuffling the deck
    @Test
    void testShuffleDeck() {
        DistrictDeck deck = new DistrictDeck();
        DistrictCard card1 = DistrictCard.DRAGON_GATE;
        DistrictCard card2 = DistrictCard.CHURCH;
        DistrictCard card3 = DistrictCard.CATHEDRAL;

        deck.add(card1);
        deck.add(card2);
        deck.add(card3);

        // Save the original order
        List<DistrictCard> originalOrder = new ArrayList<>(deck.getCards());

        // Shuffle the deck
        deck.shuffle();

        // The deck should have the same cards but in a different order
        assertNotEquals(originalOrder, deck.getCards());
        assertTrue(originalOrder.containsAll(deck.getCards()) && deck.getCards().containsAll(originalOrder));
    }

    // Test case for clearing the deck
    @Test
    void testClearDeck() {
        DistrictDeck deck = new DistrictDeck();
        DistrictCard card = DistrictCard.BATTLEFIELD;

        deck.add(card);
        assertFalse(deck.isEmpty());

        deck.clear();
        assertTrue(deck.isEmpty());
    }

    // Test case for adding a card at a random position
    @Test
    void testAddRandomCard() {
        DistrictDeck deck = new DistrictDeck();
        DistrictCard card1 = DistrictCard.FORTRESS;
        DistrictCard card2 = DistrictCard.HARBOR;

        deck.add(card1);
        deck.addRandom(card2);

        // The deck should contain both cards
        assertTrue(deck.contains(card1));
        assertTrue(deck.contains(card2));
    }
}