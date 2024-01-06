package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotWeakTest {

    Player botWeak;
    DistrictDeck districtDeck;

    @BeforeEach
    void setUp(){
        botWeak = new BotWeak();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    @Test
    void putADistrict() {
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TEMPLE);
        botWeak.getHands().add(DistrictCard.GRAVEYARD);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(20);

        //Should be Temple because its value are the smallest of the botWeak hand
        botWeak.addCardToBoard(botWeak.choiceToPutADistrict());
        assertEquals(DistrictCard.TEMPLE,botWeak.getBoard().get(0));

        //Should be Market because its value are now the smallest of the botWeak hand
        botWeak.addCardToBoard(botWeak.choiceToPutADistrict());
        assertEquals(DistrictCard.MARKET,botWeak.getBoard().get(1));
        botWeak.getHands().clear();

        //When District are equals
        botWeak.getHands().add(DistrictCard.MONASTERY);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.getHands().add(DistrictCard.KEEP);
        botWeak.getHands().add(DistrictCard.DOCKS);

        botWeak.addCardToBoard(botWeak.choiceToPutADistrict());

        //Should be Monastery because there are all equals and the order doesn't change
        assertEquals(DistrictCard.MONASTERY,botWeak.getBoard().get(2));
    }
}