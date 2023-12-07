package fr.cotedazur.univ.polytech.model.deck;

import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest {

    @Test
    void shuffle() {
        Deck<CharacterCard> deck = DeckFactory.createCharacterDeck();
        deck.shuffle();
        assertNotEquals(deck, DeckFactory.createCharacterDeck());
    }

    @Test
    void draw() {
        Deck<CharacterCard> deck = DeckFactory.createCharacterDeck();
        assertEquals(8, deck.size());
        CharacterCard card = deck.draw(1);
        assertEquals(CharacterCard.THIEF, card);
        assertEquals(7, deck.size());
        assertFalse(deck.contains(CharacterCard.THIEF));
    }
}