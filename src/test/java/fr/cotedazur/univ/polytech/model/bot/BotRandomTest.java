package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BotRandomTest {


    BotRandom botRandom2;
    BotRandom botRandom1;

    private DistrictDeck districtDeck;

    @BeforeEach
    void setUp() {
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    @Test
    void testPutADistrict() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.putADistrict();
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));
        botRandom2.getBoard().clear();

        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.putADistrict();
        assertEquals(3, botRandom2.getHands().size());
        assertEquals(1, botRandom2.getBoard().size());

        botRandom2.getHands().clear();
        assertFalse(botRandom2.putADistrict());
    }

    @Test
    void testChoiceToPutADistrictIfNoCardsInHand() {
        assertNull(botRandom2.choiceToPutADistrict());
    }

    @Test
    void testBotRandomActionIfDistrictDeckIsEmpty() {
        districtDeck.clear();
        int currentGolds1 = botRandom1.getGolds();
        int currentGolds2 = botRandom2.getGolds();
        botRandom2.startChoice(districtDeck);
        botRandom1.startChoice(districtDeck);

        //If the deck is empty the bot should collect 2 golds
        assertEquals(currentGolds1 + 2, botRandom1.getGolds());
        assertEquals(currentGolds2 + 2, botRandom2.getGolds());
    }
}