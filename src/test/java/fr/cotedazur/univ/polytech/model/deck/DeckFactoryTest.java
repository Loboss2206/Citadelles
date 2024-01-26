package fr.cotedazur.univ.polytech.model.deck;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckFactoryTest {

    @Test
    void createCharacterDeck() {
        Deck<CharacterCard> characterDeck = DeckFactory.createCharacterDeck();
        assertNotNull(characterDeck, "The character deck is null (and it shouldn't be)");
        assertEquals(8, characterDeck.size(), "The character deck doesn't have the right size");
        assertFalse(characterDeck.isEmpty(), "The character deck is empty (and it shouldn't be)");
        for (CharacterCard card : CharacterCard.values()) {
            assertTrue(characterDeck.contains(card), "The character deck doesn't contain the card " + card);
        }
        for (int i = 0; i < characterDeck.size(); i++) {
            assertNotNull(characterDeck.draw(i), "The character deck doesn't contain the card with index " + i);
        }
    }

    @Test
    void createDistrictDeck() {
        Deck<DistrictCard> districtDeck = DeckFactory.createDistrictDeck();
        assertNotNull(districtDeck, "The district deck is null (and it shouldn't be)");
        assertEquals(65, districtDeck.size(), "The district deck doesn't have the right size");
        assertFalse(districtDeck.isEmpty(), "The district deck is empty (and it shouldn't be)");
        for (DistrictCard card : DistrictCard.values()) {
            assertTrue(districtDeck.contains(card), "The district deck doesn't contain the card " + card);
        }
    }

    @Test
    void createEmptyDistrictDeck() {
        Deck<DistrictCard> emptyDistrictDeck = DeckFactory.createEmptyDistrictDeck();
        assertNotNull(emptyDistrictDeck, "The empty district deck is null (and it shouldn't be)");
        assertEquals(0, emptyDistrictDeck.size(), "The empty district deck doesn't have the right size");
        assertTrue(emptyDistrictDeck.isEmpty(), "The empty district deck is not empty (and it should be)");
    }

    @Test
    void createEmptyCharacterDeck() {
        Deck<CharacterCard> emptyCharacterDeck = DeckFactory.createEmptyCharacterDeck();
        assertNotNull(emptyCharacterDeck, "The empty character deck is null (and it shouldn't be)");
        assertEquals(0, emptyCharacterDeck.size(), "The empty character deck doesn't have the right size");
        assertTrue(emptyCharacterDeck.isEmpty(), "The empty character deck is not empty (and it should be)");
    }
}