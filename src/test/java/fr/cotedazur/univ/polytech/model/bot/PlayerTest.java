package fr.cotedazur.univ.polytech.model.bot;


import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    BotRandom botRandom1;
    BotRandom botRandom0;

    BotRandom botRandom2;
    private DistrictDeck districtDeck;


    @BeforeEach
    void setUp() {
        Player.setCount(0); // because the static variable changes during all the tests (of this class or another) which is fail this class tests
        botRandom0 = new BotRandom();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    /**
     * Test if the name is automatically and correctly created
     */
    @Test
    void testNameCreation() {
        assertEquals("BOT0", botRandom0.getName());
        assertEquals("BOT1", botRandom1.getName());
        assertEquals("BOT2", botRandom2.getName());
    }

    @Test
    void testCollect2Golds() {
        botRandom2.collectTwoGolds();
        botRandom2.collectTwoGolds();
        assertEquals(4, botRandom2.getGolds());
    }

    @Test
    void testDrawCard(){
        DistrictDeck copyDeck = new DistrictDeck();
        copyDeck = DeckFactory.createDistrictDeck();
        botRandom2.drawCard(districtDeck);

        //If the card is correctly removed
        assertEquals(66, copyDeck.size());
        assertEquals(65, districtDeck.size());
    }
}