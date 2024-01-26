package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerComparatorTest {

    BotRandom botRandom1;
    BotRandom botRandom2;

    PlayerComparator playerComparator;

    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        playerComparator = new PlayerComparator();
    }

    @Test
    void testComparatorOnTheNumberOfPoints() {
        // Bot 1 > Bot 2
        botRandom1.setPoints(10);
        botRandom2.setPoints(5);
        assertEquals(1, playerComparator.compare(botRandom1, botRandom2));

        //Bot 1 == Bot2 but character different
        botRandom1.setPlayerRole(CharacterCard.ASSASSIN);
        botRandom2.setPlayerRole(CharacterCard.BISHOP);
        botRandom2.setPoints(10);
        assertEquals(-1, playerComparator.compare(botRandom1, botRandom2));

        //Bot 1 < Bot 2
        botRandom2.setPoints(15);
        assertEquals(-1, playerComparator.compare(botRandom1, botRandom2));
    }
}