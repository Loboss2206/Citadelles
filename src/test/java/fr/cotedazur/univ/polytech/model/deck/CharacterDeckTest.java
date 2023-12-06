package fr.cotedazur.univ.polytech.model.deck;

import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.model.card.RoleCard;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest {

    @Test
    void shuffle() {
        Deck<RoleCard> deck = DeckFactory.createCharacterDeck();
        deck.shuffle();
        assertNotEquals(deck, DeckFactory.createCharacterDeck());
    }

    @Test
    void draw() {
        Deck<RoleCard> deck = DeckFactory.createCharacterDeck();
        assertEquals(8, deck.size());
        RoleCard card = deck.draw(1);
        assertEquals(RoleCard.THIEF, card);
        assertEquals(7, deck.size());
        assertFalse(deck.contains(RoleCard.THIEF));
    }
}