package fr.cotedazur.univ.polytech.model.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    BotRandom botRandom1;
    BotRandom botRandom0;

    BotRandom botRandom2;

    @BeforeEach
    void setUp() {
        Player.setId(0);//because the static variable changes during all the tests (of this class or another) which is fail this class tests
        botRandom0 = new BotRandom();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
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
}