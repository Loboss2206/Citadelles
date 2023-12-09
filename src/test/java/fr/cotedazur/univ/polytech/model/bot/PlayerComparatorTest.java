package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerComparatorTest {

    BotRandom botRandom1;
    BotRandom botRandom2;

    PlayerComparator playerComparator;

    @BeforeEach
    void setUp() {
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        playerComparator = new PlayerComparator();
    }

    @Test
    void testComparatorOnlyWithGolds() {

        //Bot 1 > Bot 2
        botRandom1.collectTwoGolds();
        assertEquals(1, playerComparator.compare(botRandom1, botRandom2));
        //Bot 1 == Bot2
        botRandom2.collectTwoGolds();
        assertEquals(0, playerComparator.compare(botRandom1, botRandom2));

        //Bot 1 < Bot 2
        botRandom2.collectTwoGolds();
        assertEquals(-1, playerComparator.compare(botRandom1, botRandom2));
    }

    @Test
    void testComparatorOnlyWithGoldsAndDistrictPlaced() {

        //Bot 1 > Bot 2
        botRandom1.collectTwoGolds();
        botRandom1.getBoard().add(DistrictCard.PALACE);
        botRandom2.collectTwoGolds();
        assertEquals(1, playerComparator.compare(botRandom1, botRandom2));
        //Bot 1 == Bot2
        botRandom2.getBoard().add(DistrictCard.PALACE);
        assertEquals(0, playerComparator.compare(botRandom1, botRandom2));

        //Bot 1 < Bot 2
        botRandom2.getBoard().add(DistrictCard.TEMPLE);
        assertEquals(-1, playerComparator.compare(botRandom1, botRandom2));
    }
}