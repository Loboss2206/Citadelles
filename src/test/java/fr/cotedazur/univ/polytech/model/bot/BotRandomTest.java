package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BotRandomTest {


    BotRandom botRandom2;

    private DistrictDeck districtDeck;

    @BeforeEach
    void setUp() {
        botRandom2 = new BotRandom();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    @Test
    void testPutADistrict() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.putADisctrict();
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));
        botRandom2.getBoard().clear();

        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.drawCard(districtDeck);
        botRandom2.putADisctrict();
        assertEquals(3, botRandom2.getHands().size());
        assertEquals(1, botRandom2.getBoard().size());
    }
}