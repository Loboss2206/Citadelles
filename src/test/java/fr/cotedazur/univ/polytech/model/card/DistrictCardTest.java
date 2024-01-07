package fr.cotedazur.univ.polytech.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictCardTest {

    @Test
    void getDistrictName() {
        assertEquals("Taverne", DistrictCard.TAVERN.getDistrictName());
        assertEquals("Champ de bataille", DistrictCard.BATTLEFIELD.getDistrictName());
        assertEquals("Cathédrale", DistrictCard.CATHEDRAL.getDistrictName());
        assertEquals("Château", DistrictCard.CASTLE.getDistrictName());
        assertEquals("Ville hantée", DistrictCard.HAUNTED_CITY.getDistrictName());
        assertEquals("Bibliothèque", DistrictCard.LIBRARY.getDistrictName());
    }

    @Test
    void getQuantityInDeck() {
        assertEquals(5, DistrictCard.TAVERN.getQuantityInDeck());
        assertEquals(3, DistrictCard.BATTLEFIELD.getQuantityInDeck());
        assertEquals(2, DistrictCard.CATHEDRAL.getQuantityInDeck());
        assertEquals(4, DistrictCard.CASTLE.getQuantityInDeck());
        assertEquals(1, DistrictCard.HAUNTED_CITY.getQuantityInDeck());
        assertEquals(1, DistrictCard.LIBRARY.getQuantityInDeck());
    }

    @Test
    void getDistrictValue() {
        assertEquals(1, DistrictCard.TAVERN.getDistrictValue());
        assertEquals(3, DistrictCard.BATTLEFIELD.getDistrictValue());
        assertEquals(5, DistrictCard.CATHEDRAL.getDistrictValue());
        assertEquals(4, DistrictCard.CASTLE.getDistrictValue());
        assertEquals(2, DistrictCard.HAUNTED_CITY.getDistrictValue());
        assertEquals(6, DistrictCard.LIBRARY.getDistrictValue());
    }

    @Test
    void getDistrictColor() {
        assertEquals(Color.GREEN, DistrictCard.TAVERN.getDistrictColor());
        assertEquals(Color.RED, DistrictCard.BATTLEFIELD.getDistrictColor());
        assertEquals(Color.BLUE, DistrictCard.CATHEDRAL.getDistrictColor());
        assertEquals(Color.YELLOW, DistrictCard.CASTLE.getDistrictColor());
        assertEquals(Color.PURPLE, DistrictCard.HAUNTED_CITY.getDistrictColor());
        assertEquals(Color.PURPLE, DistrictCard.LIBRARY.getDistrictColor());
    }
}
