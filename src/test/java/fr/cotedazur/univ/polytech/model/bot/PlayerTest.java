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

    @Test
    void testAddCardOnTheBoard() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.addCardToBoard("TRADING_POST");
        assertNotNull(botRandom2.getBoard().get(0));
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));

        botRandom2.getBoard().clear();
        botRandom2.addCardToBoard("TRADING_POST");
        assertEquals(botRandom2.getBoard().size(), 0);
    }

    @Test
    void hasCardOnTheBoard() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.addCardToBoard("TRADING_POST");
        assertNotNull(botRandom2.getBoard().get(0));
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));
        assertTrue(botRandom2.hasCardOnTheBoard("TRADING_POST"));

        botRandom2.getBoard().clear();
        assertFalse(botRandom2.hasCardOnTheBoard("TRADING_POST"));
    }

    @Test
    void hasPlayableCard() {
        botRandom2.getHands().clear();
        assertFalse(botRandom2.hasPlayableCard());

        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        assertTrue(botRandom2.hasPlayableCard());

        botRandom2.addCardToBoard("TRADING_POST");
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        assertFalse(botRandom2.hasPlayableCard());
    }
}